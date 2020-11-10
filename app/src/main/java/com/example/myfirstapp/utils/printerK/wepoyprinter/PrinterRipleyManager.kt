package com.example.myfirstapp.utils.printerK.wepoyprinter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cl.mbaas.baytex.api.utils.PrintUtils
import cl.mbaas.baytex.api.utils.PrintUtils.LineType
import cl.mbaas.baytex.api.utils.PrintUtils.TextAlign
import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.CloseCartResponse
import com.example.myfirstapp.utils.Methods.Companion.generateQRCode
import com.example.myfirstapp.utils.printerK.PrinterToTicket
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import java.util.*

/**
 * Created by agustin on 7/19/17.
 */
class PrinterRipleyManager(
    mContext: Context,
    listener: IPrinterListener
) : PrinterToTicket(listener, mContext) {
    private val ripleyPrinter = RipleyPrinter()
    private fun configPrinter() {
        ripleyPrinter.setSpeedLevel(62)
        ripleyPrinter.setGrayLevel(30)
        ripleyPrinter.setupPage(384, -1)
    }

    private fun checkPrinterStatus(): Boolean {
        return when (ripleyPrinter.status) {
            0 -> true
            -1 -> {
                listener.error("Impresora sin papel")
                false
            }
            -2 -> {
                listener.error("Impresora recalentada")
                false
            }
            -3 -> {
                listener.error("Bajo Voltaje")
                false
            }
            -4 -> {
                listener.error("Impresora ocupada")
                false
            }
            -257 -> {
                listener.error("Error driver")
                false
            }
            else -> {
                listener.error("Estado de la impresora desconocido")
                false
            }
        }
    }

    private fun printTicketCambio(
        jsonNode: JsonNode,
        num: Long,
        print: Boolean
    ) {
        if (checkPrinterStatus()) {
            try {
                try {
                    if (print) {
                        ripleyPrinter.lineBreak()
                        ripleyPrinter.lineBreak()
                    }
                    if (jsonNode.hasNonNull("lineas")) {
                        val ticketCambio: MutableList<JsonNode> =
                            ArrayList()
                        for (i in 0 until jsonNode["lineas"].size()) {
                            val linea = jsonNode["lineas"][i]
                            if (linea.hasNonNull("text") && (linea["text"].asText()
                                    .startsWith("SUCURSAL") || linea["text"].asText()
                                    .startsWith("FECHA EMISION"))
                            ) {
                                ticketCambio.add(linea)
                            }
                            if (linea.hasNonNull("tipo") && linea["tipo"].asText()
                                    .equals("BARCODE_GS1_128", ignoreCase = true)
                            ) {
                                ticketCambio.add(linea)
                            }
                        }
                        if (ticketCambio.size == 3) {
                            for (i in 0 until num) {
                                ripleyPrinter.lineBreak()
                                ripleyPrinter.textAlign = TextAlign.CENTER
                                ripleyPrinter.drawText("TICKET DE CAMBIO")
                                ripleyPrinter.lineBreak()
                                for (l in ticketCambio.indices) {
                                    ripleyPrinter.resetStyle()
                                    val linea = ticketCambio[l]
                                    if (linea.hasNonNull("tipo") && linea["tipo"].asText()
                                            .equals("BARCODE_GS1_128", ignoreCase = true)
                                    ) {
                                        ripleyPrinter.lineBreak()
                                    }
                                    printJsonLine(linea, null)
                                }
                                ripleyPrinter.lineBreak()
                                ripleyPrinter.lineBreak()
                                if (i + 1.toLong() != num) ripleyPrinter.drawCut()
                            }
                            if (print) ripleyPrinter.printPage()
                        }
                    }
                } catch (e: Exception) {
                    listener.error("Error al imprimir la boleta")
                }
                listener.endPrint()
            } catch (e: Exception) {
                listener.error("Ocurrio un error al imprimir")
            }
        }
    }

    override fun printComprobante(comprobante: CloseCartResponse?) {
        if (checkPrinterStatus()) {
            try {
                val tagLogo = "http://logo"
                val lines: MutableList<ObjectNode> =
                    ArrayList()
                for (l in comprobante!!.clientVoucher.indices) {
                    val mapper = ObjectMapper()
                    val node2 =
                        mapper.convertValue(
                            comprobante.clientVoucher[l],
                            ObjectNode::class.java
                        )
                    lines.add(node2)
                }
                val imagesJson: MutableMap<String, Bitmap> =
                    HashMap()
                imagesJson[tagLogo] =
                    BitmapFactory.decodeResource(mContext.resources, R.drawable.logo_app)
                for (l in lines.indices) {
                    ripleyPrinter.resetStyle()
                    printJsonLine(lines[l], imagesJson)
                }
                ripleyPrinter.lineBreak()
                ripleyPrinter.lineBreak()
                ripleyPrinter.printPage()
                listener.endPrint()
            } catch (e: Exception) {
                listener.error("Ocurrio un error al imprimir")
            }
        }
    }

    override fun printBoletaJson(
        jsonNode: JsonNode,
        imagesJson: Map<String, Bitmap>?,
        numTickets: Long
    ) {
        if (checkPrinterStatus()) {
            Thread(Runnable {
                try {
                    if (jsonNode.hasNonNull("lineas")) {
                        val ticketCambio: MutableList<JsonNode> =
                            ArrayList()
                        val ofertasData: MutableList<JsonNode> =
                            ArrayList()
                        var haveCut = false
                        for (i in 0 until jsonNode["lineas"].size()) {
                            ripleyPrinter.resetStyle()
                            val linea = jsonNode["lineas"][i]
                            printJsonLine(linea, imagesJson)
                            if (linea.hasNonNull("text") && (linea["text"].asText()
                                    .startsWith("SUCURSAL") || linea["text"].asText()
                                    .startsWith("FECHA EMISION"))
                            ) {
                                ticketCambio.add(linea)
                                if (ofertasData.size < 3) // puede venir el ticket de cambio
                                    ofertasData.add(0, linea)
                            }
                            if (linea.hasNonNull("text") && linea["text"].asText()
                                    .startsWith("NRO TRANSACCION")
                            ) {
                                ofertasData.add(linea)
                            }
                            if (linea.hasNonNull("tipo") && linea["tipo"].asText()
                                    .equals("BARCODE_GS1_128", ignoreCase = true)
                            ) {
                                ticketCambio.add(linea)
                            }
                            if (linea.hasNonNull("tipo") && linea["tipo"].asText()
                                    .equals("CUT", ignoreCase = true)
                            ) {
                                haveCut = true
                            }
                        }
                        if (numTickets > 0) {
                            ripleyPrinter.drawCut()
                            printTicketCambio(jsonNode, numTickets, false)
                            ripleyPrinter.lineBreak()
                        } else {
                            ripleyPrinter.lineBreak()
                            ripleyPrinter.lineBreak()
                        }
                    }
                    ripleyPrinter.printPage()
                    listener.endPrint()
                } catch (e: Exception) {
                    listener.error("Ocurrio un error al imprimir")
                }
            }).start()
        }
    }

    override fun printTicketCambio(boletaJson: JsonNode, num: Long) {
        printTicketCambio(boletaJson, num, true)
    }

    override fun connect() {
        listener.connectedPrinter()
    }

    override fun disconnect() {

    }

    private fun printJsonLine(line: JsonNode, imagesJson: Map<String, Bitmap>?) {
        if (line.hasNonNull("tipo")) {
            val lineType: LineType? = try {
                LineType.valueOf(line["tipo"].asText().toUpperCase())
            } catch (e: Exception) {
                null
            }
            @Suppress("IntroduceWhenSubject")
            when {
                lineType == LineType.TEXT -> {
                    readAlign(line)
                    readSize(line)
                    readBold(line)
                    if (line.hasNonNull("text")) {
                        var text = line["text"].asText()
                        if (line.hasNonNull("text") && line["text"].asText()
                                .startsWith("NRO UNICO")
                        ) {
                            ripleyPrinter.textSize = PrintUtils.TextSize.SMALL
                        }
                        if (line.hasNonNull("text") && line["text"].asText()
                                .startsWith("FECHA EMISION")
                        ) {
                            text = text.replace("HORA: ", "").replace("FECHA EMISION", "FECHA")
                        }
                        if (line.hasNonNull("text")) ripleyPrinter.drawText(text)
                    }
                }
                lineType == LineType.TEXT_COLUMN_2 -> {
                    readAlign(line)
                    readSize(line)
                    readBold(line)
                    if (line["text1"] != null && line["text2"] != null) {
                        if (!line["text1"].asText().isEmpty() && !line["text2"].asText()
                                .isEmpty()
                        ) {
                            if (line["text1"].asText()
                                    .startsWith("Nombre Cliente") && !line["text2"].asText()
                                    .startsWith(" 0 0")
                            ) {
                                ripleyPrinter.drawTextLeftRight(
                                    line["text1"].asText(),
                                    line["text2"].asText()
                                )
                            } else if (!line["text1"].asText().startsWith("Nombre Cliente")) {
                                ripleyPrinter.drawTextLeftRight(
                                    line["text1"].asText(),
                                    line["text2"].asText()
                                )
                            }
                        }
                    }
                }
                lineType == LineType.TEXT_COLUMN_3 -> {
                    readAlign(line)
                    readSize(line)
                    readBold(line)
                    if (line["text1"] != null && line["text3"] != null) {
                        if (!line["text1"].asText().isEmpty() || !line["text3"].asText()
                                .isEmpty()
                        ) ripleyPrinter.drawTextLeftRight(
                            line["text1"].asText(),
                            line["text3"].asText()
                        )
                    }
                    if (line["text2"] != null) {
                        if (!line["text2"].asText().isEmpty()) ripleyPrinter.drawText(
                            line["text2"].asText().replace("CANTIDAD", "CANT.")
                        )
                    }
                }
                lineType == LineType.TEXT_LEFT_RIGHT -> {
                    readAlign(line)
                    readSize(line)
                    readBold(line)
                    var textRight: String? = " "
                    if (line.hasNonNull("textRight")) textRight = line["textRight"].asText()
                    var textLeft: String? = " "
                    if (line.hasNonNull("textLeft")) textLeft = line["textLeft"].asText()
                    ripleyPrinter.drawTextLeftRight(textLeft, textRight)
                }
                lineType == LineType.IMAGE_TEXT_RIGHT -> if (imagesJson != null && line.hasNonNull("imagen") && line["imagen"].asText()
                        .startsWith("http") && line.hasNonNull("texts")
                ) {
                    val image = imagesJson[line["imagen"].asText()]
                    if (image != null) {
                        val texts = line["texts"]
                        ripleyPrinter.drawBitmap(PrintUtils.fixTransparence(image))
                        ripleyPrinter.resetStyle()
                        var i = 0
                        while (i < texts.size()) {
                            ripleyPrinter.drawText(texts[i].asText())
                            i++
                        }
                    }
                }
                lineType == LineType.IMAGE -> {
                    readAlign(line)
                    if (line.hasNonNull("imagen") && line["imagen"].asText()
                            .startsWith("http")
                    ) {
                        val image = imagesJson!![line["imagen"].asText()]
                        if (image != null) {
                            ripleyPrinter.drawBitmap(PrintUtils.fixTransparence(image))
                        }
                    }
                }
                lineType == LineType.LINE -> ripleyPrinter.drawLine()
                lineType == LineType.DOT_LINE -> ripleyPrinter.drawDotLine()
                lineType == LineType.LINE_BREAK -> ripleyPrinter.lineBreak()
                lineType == LineType.SYMBOL_PDF417_STANDARD -> ripleyPrinter.drawPDF417(
                    line["value"].asText()
                )
                lineType == LineType.BARCODE_GS1_128 -> try {
                    val image = PrintUtils.code128(line["value"].asText())
                    ripleyPrinter.drawBitmap(image)
                    ripleyPrinter.textAlign = TextAlign.CENTER
                    ripleyPrinter.drawText(line["value"].asText())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                lineType == LineType.CUT -> {
                    ripleyPrinter.lineBreak()
                    ripleyPrinter.lineBreak()
                }
                else -> if (line["tipo"] != null) {
                    if (line["tipo"].asText() == "CODE_TEXT_QR") {
                        try {
                            val image =
                                generateQRCode(line["value"].asText())
                            ripleyPrinter.drawBitmap(image)
                            ripleyPrinter.textAlign = TextAlign.CENTER
                            //ripleyPrinter.drawText(line["value"].asText()) //todo se comento para quitar el texto debajo del urobo
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    private fun readSize(line: JsonNode) {
        if (line.hasNonNull("size")) {
            try {
                ripleyPrinter.textSize = PrintUtils.TextSize.valueOf(line["size"].asText())
                return
            } catch (e: Exception) {
            }
        }
        ripleyPrinter.textSize = PrintUtils.TextSize.NORMAL
    }

    private fun readAlign(line: JsonNode) {
        if (line.hasNonNull("align")) {
            try {
                ripleyPrinter.textAlign = TextAlign.valueOf(
                    line["align"].asText().toUpperCase()
                )
                return
            } catch (e: Exception) {
            }
        }
        ripleyPrinter.textAlign = TextAlign.LEFT
    }

    private fun readBold(line: JsonNode) {
        ripleyPrinter.isBold = line.hasNonNull("bold") && line["bold"].asInt() == 1
    }

    init {
        configPrinter()
    }
}