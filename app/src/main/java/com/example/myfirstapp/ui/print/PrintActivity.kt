package com.example.myfirstapp.ui.print

import android.app.Activity
import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextPaint
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapp.R
import com.example.myfirstapp.ui.print.helper.BTPrinter
import com.example.myfirstapp.ui.print.helper.IBTPrinter
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.oned.Code128Writer
import com.pax.gl.extprinter.exception.PrintException
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import pe.com.viergegroup.rompefilassdk.util.Common
import pe.com.viergegroup.rompefilassdk.util.RxUtils
import com.example.myfirstapp.ui.print.models.OrderTicketLineModel
import com.example.myfirstapp.ui.print.models.PrintRequestModel
import com.example.myfirstapp.ui.print.models.PrintResultModel
import com.example.myfirstapp.utils.Methods
import java.util.*

class PrintActivity: AppCompatActivity() {

    private val _tag = PrintActivity::class.java.simpleName

    private val _extraName = "PrintResponse"

    private val _textPaint = TextPaint()

    private val _maxWidth = 384
    private val _textSizeSmall = 16F
    private val _textSizeNormal = 19F

    private val _printResultModel: PrintResultModel = PrintResultModel()

    private val _btPrinter: IBTPrinter = BTPrinter()

    private val _gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or  View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        )

        setContentView(R.layout.loading);

        val data: Bundle? = intent.extras

        _textPaint.textSize = _textSizeNormal
        _textPaint.isFakeBoldText = false
        _textPaint.color = Color.BLACK
        _textPaint.isAntiAlias = true
        _textPaint.textAlign = Paint.Align.LEFT

        try {
            val printRequest = _gson.fromJson(data?.getString("print"), PrintRequestModel::class.java) ?: throw Exception("Bad Request")

            val bitmap = generateBitmap(printRequest.Ticket)
            //sendResult()
            printRequest.Mac?.let { printBitmap(it, bitmap) }

        } catch (e: Exception) {
            e.message?.let { Log.d(_tag, it) }
            _printResultModel.Estado = 0
            _printResultModel.Mensaje = e.message
            sendResult()
        }
    }

    private fun sendResult() {

        _btPrinter.disconect()

        val activityIntent = intent
        activityIntent.putExtra(_extraName, _gson.toJson(_printResultModel))
        setResult(Activity.RESULT_OK, activityIntent)
        finish()
    }

    private fun generateBitmap(ticket: List<OrderTicketLineModel>?): Bitmap {
        var bitmapTicket = textAsBitmap()
        if (ticket != null) {
            transaction@ for (line in ticket) {
                when (line.tipo) {
                    "BARCODE_GS1_128" -> {
                        if (isNullOrEmpty(line.values)) {
                            continue@transaction
                        }

                        var hints: Map<EncodeHintType?, Any?>? = null
                        hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
                        hints.put(EncodeHintType.CHARACTER_SET, line.values)
                        val bitMatrix: BitMatrix = Code128Writer().encode(line.values, BarcodeFormat.CODE_128, _maxWidth, 50, hints)
                        val width: Int = bitMatrix.width
                        val height: Int = bitMatrix.height
                        val pixels = IntArray(width * height)

                        for (y in 0 until height) {
                            val offset = y * width
                            for (x in 0 until width) {
                                pixels[offset + x] = if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
                            }
                        }
                        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)

                        bitmapTicket = mergeBitmap(bitmapTicket, bitmap)
                    }
                    "BASE64" -> {
                        if (isNullOrEmpty(line.text)) {
                            continue@transaction
                        }

                        val bitmap = Common.string64ToBitmap(line.text)
                        bitmapTicket = mergeBitmap(bitmapTicket, bitmap)
                    }
                    "TEXT" -> {
                        if (isNullOrEmpty(line.text)) {
                            continue@transaction
                        }

                        var text : String = line.text!!
                        val align : String = line.align!!
                        val bold : String = line.bold!!
                        var textSize = _textSizeNormal

                        if (text.startsWith("NRO UNICO", true)) {
                            textSize = _textSizeSmall
                        }

                        if (text.startsWith("FECHA EMISION"))
                        {
                            text = text.replace("HORA: ", "").replace("FECHA EMISION", "FECHA")
                        }

                        val textList = processText(text, bold, textSize)

                        for (line in textList) {
                            val bitmap = textAsBitmap(line, align, bold, textSize)
                            bitmapTicket = mergeBitmap(bitmapTicket, bitmap)
                        }
                    }
                    "TEXT_COLUMN_2" -> {
                        if (isNullOrEmpty(line.text1) && isNullOrEmpty(line.text2)) {
                            continue@transaction
                        }
                        if (isNullOrEmpty(line.text1)) {
                            line.text1 = ""
                        }
                        if (isNullOrEmpty(line.text2)) {
                            line.text2 = ""
                        }

                        val text1 = line.text1!!
                        val text2 = line.text2!!
                        val bold = line.bold!!

                        var bitmap: Bitmap? = null
                        if ((!text1.startsWith("Nombre Cliente")) || (text1.startsWith("Nombre Cliente") && !text2.startsWith(" 0 0"))) {
                            bitmap = textLeftRightAsBitmap(text1, text2, bold, _textSizeNormal)
                        }

                        if (bitmap == null) {
                            continue@transaction
                        }

                        bitmapTicket = mergeBitmap(bitmapTicket, bitmap)
                    }
                    "TEXT_LEFT_RIGHT" -> {
                        if (isNullOrEmpty(line.textLeft) && isNullOrEmpty(line.textRight)) {
                            continue@transaction
                        }
                        if (isNullOrEmpty(line.textLeft)) {
                            line.textLeft = ""
                        }
                        if (isNullOrEmpty(line.textRight)) {
                            line.textRight = ""
                        }

                        val textLeft = line.textLeft!!
                        val textRight = line.textRight!!
                        val bold = line.bold!!

                        val bitmap = textLeftRightAsBitmap(textLeft, textRight, bold, _textSizeNormal)
                        bitmapTicket = mergeBitmap(bitmapTicket, bitmap)
                    }
                    "LINE" -> {
                        val text = "------------------------------"

                        val bitmap = textAsBitmap(text)
                        bitmapTicket = mergeBitmap(bitmapTicket, bitmap)
                    }
                    "LINE_BREAK" -> {
                        val bitmap = textAsBitmap()
                        bitmapTicket = mergeBitmap(bitmapTicket, bitmap)
                    }
                    "CUT" -> {
                        for (i in 1..5) {
                            val bitmap = textAsBitmap()
                            bitmapTicket = mergeBitmap(bitmapTicket, bitmap)
                        }
                    }
                    "CODE_TEXT_QR"-> {
                        try {
                            val bitmap = Methods.generateQRCode(line.values.toString(), 385, 220)
                            /*val final = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height);*/
                            bitmapTicket = mergeBitmap(bitmapTicket, bitmap)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
        return bitmapTicket
    }

    private fun isNullOrEmpty(str: String?): Boolean {
        return (str == null || str.trim().isEmpty())
    }

    private fun getExceptionMessage(message: String?): String {
        if (message != null) {
            if (message.contains("PRINT#-1("))
            {
                return "Error desconocido (1)."
            }
            if (message.contains("PRINT#-2("))
            {
                return "Error de transmisión (3)."
            }
            if (message.contains("PRINT#-3("))
            {
                return "Error de transmisión (2)."
            }
            if (message.contains("PRINT#-4("))
            {
                return "Impresión incompleta."
            }
            if (message.contains("PRINT#-5("))
            {
                return "Impresora sin papel."
            }
            if (message.contains("PRINT#-6("))
            {
                return "Error de transmisión (1)."
            }
            if (message.contains("PRINT#-7("))
            {
                return "Impresora sobrecalentada."
            }
            if (message.contains("PRINT#-8("))
            {
                return "Fallo de impresora."
            }
            if (message.contains("PRINT#-9("))
            {
                return "Impresora con bajo voltaje."
            }
            if (message.contains("PRINT#-10("))
            {
                return "Impresora ocupada."
            }
            if (message.contains("PRINT#-11("))
            {
                return "Error en fuentes de impresora."
            }
            if (message.contains("PRINT#-12("))
            {
                return "Error de conexión."
            }
        }
        return "Error desconocido (2)."
    }

    private fun printBitmap(mac: String, bitmap: Bitmap) {
        RxUtils.addDisposable(
            _btPrinter.connect(mac).flatMap { it ->
                if (it) _btPrinter.printBitmap(bitmap) else Observable.error(PrintException(-12))
            }.subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread())
            .subscribe({
                _printResultModel.Estado = 1
                _printResultModel.Mensaje = ""
                Handler(Looper.getMainLooper()).post {
                    sendResult()
                }
            }) { e ->
                _printResultModel.Estado = 0
                _printResultModel.Mensaje = getExceptionMessage(e.message)
                Handler(Looper.getMainLooper()).post {
                    sendResult()
                }
            }
        )
    }

    private fun textAsBitmap(text: String = " ", textAlign: String = "LEFT", bold: String = "0", textSize: Float = _textSizeNormal): Bitmap {
        _textPaint.textSize = textSize
        _textPaint.isFakeBoldText = (bold == "1")

        val baseline = (-1 * _textPaint.ascent())
        val width = (_textPaint.measureText(text) + 0.5F)
        Log.d(_tag, "width: $width")
        val height = (baseline + _textPaint.descent() + 0.5F)
        val image = Bitmap.createBitmap(_maxWidth, height.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.drawColor(Color.WHITE)
        var x = 0.0F
        if (textAlign == "CENTER") {
            x = ((_maxWidth - width) / 2)
        }
        if (textAlign == "RIGHT") {
            x = (_maxWidth - width)
        }

        canvas.drawText(text, x, baseline, _textPaint)
        return image;
    }

    private fun textLeftRightAsBitmap(textLeft: String, textRight: String, bold: String, textSize: Float): Bitmap {
        _textPaint.textSize = textSize
        _textPaint.isFakeBoldText = (bold == "1")

        val baseline = (-1 * _textPaint.ascent())
        val width = (_textPaint.measureText(textRight) + 0.5F)
        val height = (baseline + _textPaint.descent() + 0.5F)
        val image = Bitmap.createBitmap(_maxWidth, height.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.drawColor(Color.WHITE)
        canvas.drawText(textLeft, 0.0F, baseline, _textPaint)
        canvas.drawText(textRight, (_maxWidth - width), baseline, _textPaint)
        return image
    }

    private fun processText(text: String, bold: String = "0", textSize: Float = _textSizeNormal): List<String> {
        _textPaint.textSize = textSize
        _textPaint.isFakeBoldText = (bold == "1")

        val processed = mutableListOf<String>()

        val splited = text.split("\n")
        for (chunk: String in splited) {
            var line = chunk
            if ((_textPaint.measureText(line) + 0.5F) > _maxWidth) {
                var charLength = 0
                while (true) {
                    val width = (_textPaint.measureText(line.substring(0, line.length - charLength)) + 0.5F)
                    if (width > _maxWidth) {
                        charLength++
                        continue
                    } else {
                        if (charLength > 0) {
                            processed.add(line.substring(0, line.length - charLength))
                            line = line.substring(line.length - charLength)
                            charLength = 0
                            continue
                        } else {
                            if (line.isNotEmpty()) {
                                processed.add(line)
                            }
                            break
                        }
                    }
                }
            } else {
                if (line.isNotEmpty()) {
                    processed.add(line)
                }
            }
        }
        return processed.toList()
    }

    private fun mergeBitmap(main: Bitmap, toAdd: Bitmap): Bitmap {
        val final = Bitmap.createBitmap(main.width, main.height + toAdd.height, main.config)
        val canvas = Canvas(final)
        canvas.drawBitmap(main, Matrix(), null)
        canvas.drawBitmap(toAdd, 0F, main.height.toFloat(), null)
        return final
    }
}