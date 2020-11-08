package com.example.myfirstapp.utils;

import android.device.PrinterManager;
import android.graphics.Bitmap;

import cl.mbaas.baytex.api.utils.PrintUtils;


public class WepoyPrinter {

    private PrinterManager printerManager = new PrinterManager();

    private int positionY = 0;

    private PrintUtils.TextSize textSize = PrintUtils.TextSize.NORMAL;

    private boolean underline = false;
    private boolean italic = false;
    private boolean bold = false;
    private boolean reverseEffect = false;
    private boolean strikeOut = false;

    private PrintUtils.TextAlign textAlign;

    private String fontName = "arial";

    public WepoyPrinter() {
        configPrinter();
    }

    private void configPrinter() {
        printerManager.close();
        printerManager.open();
        printerManager.setSpeedLevel(62);
        printerManager.setGrayLevel(30);
        printerManager.setupPage(384, -1);
    }
    public void drawText(String data) {
        if(textAlign == PrintUtils.TextAlign.LEFT) {
            positionY += printerManager.drawTextEx(data, 0, positionY, 384, -1, fontName, textSize.getValue(), 0, getStyle(), 0);
        }else {
            Bitmap b = PrintUtils.textAsBitmap(data, textSize, textAlign, bold);
            drawBitmap(b);
        }
    }
    public void drawTextLeftRight(String dataLeft, String dataRight) {
        Bitmap b = PrintUtils.textLeftRightAsBitmap(dataLeft, dataRight, textSize, bold, null);
        //positionY += printerManager.drawTextEx(data, 0, positionY,384,-1, fontName, fontSize.getValue(), 0, getStyle(), 0);

        drawBitmap(b);
    }

    public void drawLine() {
        positionY += 2;
        positionY += printerManager.drawTextEx("------------------------------------------------------", 0, positionY, 384, -1, fontName, 20, 0, 0, 0);
        positionY += 8;

    }
    public void drawCut() {
        positionY += 2;
        Bitmap b = PrintUtils.textAsBitmap("- - - - - - - - - - - - - - - - - - - - - - - - - - - ", PrintUtils.TextSize.NORMAL, PrintUtils.TextAlign.CENTER, false);
        drawBitmap(b);
        positionY += 8;

    }
    public void drawDotLine() {
        positionY += 2;
        Bitmap b = PrintUtils.textAsBitmap("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", PrintUtils.TextSize.NORMAL, PrintUtils.TextAlign.CENTER, false);
        drawBitmap(b);
        positionY += 8;

    }

    public void lineBreak() {
        positionY += printerManager.drawTextEx("     ", 0, positionY, 384, -1, fontName, textSize.getValue(), 0, getStyle(), 0);
    }

    public void drawBitmap(Bitmap image) {
        printerManager.drawBitmap(image, (384 - image.getWidth()) / 2, positionY);

        positionY += image.getHeight();
    }
    public void drawPDF417(Bitmap image) {
        printerManager.drawBitmap(image, (384 - image.getWidth()) / 2, positionY);

        positionY += image.getHeight();
    }

    public void drawPDF417(String data) {
        printerManager.drawBarcode(data, 25, positionY, 55, 1, 206, 0); //BARCODE_PDF417 55

        positionY += 20;
        positionY += 210;
    }

    private int getStyle() {
        int style = 0;
        if (bold)// en la documentacion esta cambiado
            style += 1;
        if (italic)
            style += 2;
        if (underline)
            style += 4;
        if (reverseEffect)
            style += 8;
        if (strikeOut)
            style += 16;

        return style;
    }

    public void resetStyle() {
        underline = false;
        italic = false;
        bold = false;
        reverseEffect = false;
        strikeOut = false;

        textSize = PrintUtils.TextSize.NORMAL;
        textAlign = PrintUtils.TextAlign.LEFT;
    }

    public void setSpeedLevel(int speedLevel) {
        printerManager.setSpeedLevel(speedLevel);
    }

    public void setGrayLevel(int grayLevel) {
        printerManager.setGrayLevel(grayLevel);
    }

    public void setupPage(int width, int height) {
        printerManager.setupPage(width, height);
    }

    public int getStatus() {
        return printerManager.getStatus();
    }

    public void printPage() {
        lineBreak();
        lineBreak();
        lineBreak();
        lineBreak();
        positionY = 0;

        printerManager.printPage(0);

        configPrinter();
    }

    public PrintUtils.TextSize getTextSize() {
        return textSize;
    }

    public void setTextSize(PrintUtils.TextSize textSize) {
        this.textSize = textSize;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isReverseEffect() {
        return reverseEffect;
    }

    public void setReverseEffect(boolean reverseEffect) {
        this.reverseEffect = reverseEffect;
    }

    public boolean isStrikeOut() {
        return strikeOut;
    }

    public void setStrikeOut(boolean strikeOut) {
        this.strikeOut = strikeOut;
    }


    public PrintUtils.TextAlign getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(PrintUtils.TextAlign textAlign) {
        this.textAlign = textAlign;
    }
}
