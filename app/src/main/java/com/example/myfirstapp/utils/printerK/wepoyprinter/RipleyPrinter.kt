package com.example.myfirstapp.utils.printerK.wepoyprinter

import android.device.PrinterManager
import android.graphics.Bitmap
import cl.mbaas.baytex.api.utils.PrintUtils
import cl.mbaas.baytex.api.utils.PrintUtils.TextAlign

@Suppress("MemberVisibilityCanBePrivate")
class RipleyPrinter {
    val printerManager = PrinterManager()
    var positionY = 0
    var textSize = PrintUtils.TextSize.NORMAL
    var isUnderline = false
    var isItalic = false
    var isBold = false
    var isReverseEffect = false
    var isStrikeOut = false
    var textAlign: TextAlign? = null
    var fontName = "arial"

    private fun configPrinter() {
        printerManager.close()
        printerManager.open()
        printerManager.setSpeedLevel(62)
        printerManager.setGrayLevel(30)
        printerManager.setupPage(384, -1)
    }

    fun drawText(data: String?) {
        if (textAlign == TextAlign.LEFT) {
            positionY += printerManager.drawTextEx(
                data,
                0,
                positionY,
                384,
                -1,
                fontName,
                textSize.value,
                0,
                style,
                0
            )
        } else {
            val b = PrintUtils.textAsBitmap(data, textSize, textAlign, isBold)
            drawBitmap(b)
        }
    }

    fun drawTextLeftRight(dataLeft: String?, dataRight: String?) {
        val b = PrintUtils.textLeftRightAsBitmap(dataLeft, dataRight, textSize, isBold, null)
        //positionY += printerManager.drawTextEx(data, 0, positionY,384,-1, fontName, fontSize.getValue(), 0, getStyle(), 0);
        drawBitmap(b)
    }

    fun drawLine() {
        positionY += 2
        positionY += printerManager.drawTextEx("------------------------------------------------------",
            0,
            positionY,
            384,
            -1,
            fontName,
            20,
            0,
            0,
            0
        )
        positionY += 8
    }

    fun drawCut() {
        positionY += 2
        val b = PrintUtils.textAsBitmap("- - - - - - - - - - - - - - - - - - - - - - - - - - - ",
            PrintUtils.TextSize.NORMAL,
            TextAlign.CENTER,
            false
        )
        drawBitmap(b)
        positionY += 8
    }

    fun drawDotLine() {
        positionY += 2
        val b = PrintUtils.textAsBitmap("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ",
            PrintUtils.TextSize.NORMAL,
            TextAlign.CENTER,
            false
        )
        drawBitmap(b)
        positionY += 8
    }

    fun lineBreak() {
        positionY += printerManager.drawTextEx(
            "     ",
            0,
            positionY,
            384,
            -1,
            fontName,
            textSize.value,
            0,
            style,
            0
        )
    }

    fun drawBitmap(image: Bitmap) {
        printerManager.drawBitmap(image, (384 - image.width) / 2, positionY)
        positionY += image.height
    }

    fun drawPDF417(image: Bitmap) {
        printerManager.drawBitmap(image, (384 - image.width) / 2, positionY)
        positionY += image.height
    }

    fun drawPDF417(data: String?) {
        printerManager.drawBarcode(data, 25, positionY, 55, 1, 206, 0) //BARCODE_PDF417 55
        positionY += 20
        positionY += 210
    }

    // en la documentacion esta cambiado
    private val style: Int get() {
        var style = 0
        if (isBold) // en la documentacion esta cambiado
            style += 1
        if (isItalic) style += 2
        if (isUnderline) style += 4
        if (isReverseEffect) style += 8
        if (isStrikeOut) style += 16
        return style
    }

    fun resetStyle() {
        isUnderline = false
        isItalic = false
        isBold = false
        isReverseEffect = false
        isStrikeOut = false
        textSize = PrintUtils.TextSize.NORMAL
        textAlign = TextAlign.LEFT
    }

    fun setSpeedLevel(speedLevel: Int) {
        printerManager.setSpeedLevel(speedLevel)
    }

    fun setGrayLevel(grayLevel: Int) {
        printerManager.setGrayLevel(grayLevel)
    }

    fun setupPage(width: Int, height: Int) {
        printerManager.setupPage(width, height)
    }

    val status: Int get() = printerManager.status

    fun printPage() {
        lineBreak()
        lineBreak()
        lineBreak()
        lineBreak()
        positionY = 0
        printerManager.printPage(0)
        configPrinter()
    }

    init {
        configPrinter()
    }
}