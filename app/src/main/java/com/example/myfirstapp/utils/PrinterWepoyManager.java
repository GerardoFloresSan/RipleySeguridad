package com.example.myfirstapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;
import cl.mbaas.baytex.api.utils.PrintUtils;
import cl.mbaas.baytex.api.utils.PrintUtils.LineType;
import static cl.mbaas.baytex.api.utils.PrintUtils.TextAlign.LEFT;
import static cl.mbaas.baytex.api.utils.PrintUtils.TextSize.NORMAL;
import static cl.mbaas.baytex.api.utils.PrintUtils.TextSize.SMALL;
import static cl.mbaas.baytex.api.utils.PrintUtils.code128;
import static cl.mbaas.baytex.api.utils.PrintUtils.fixTransparence;

public class PrinterWepoyManager {

    private WepoyPrinter wepoyPrinter = new WepoyPrinter();

    public PrinterWepoyManager(Context mContext) {
        configPrinter();
    }

    private void configPrinter() {
        wepoyPrinter.setSpeedLevel(62);
        wepoyPrinter.setGrayLevel(30);
        wepoyPrinter.setupPage(384, -1);
    }

    private void printJsonLine(JsonNode line, Map<String, Bitmap> imagesJson) {
        if (line.hasNonNull("tipo")) {
            LineType lineType;
            try {
                lineType = LineType.valueOf(line.get("tipo").asText().toUpperCase());
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            switch (lineType) {
                case TEXT:
                    readAlign(line);
                    readSize(line);
                    readBold(line);
                    if (line.hasNonNull("text")) {
                        String text = line.get("text").asText();
                        if (line.hasNonNull("text") && (line.get("text").asText().startsWith("NRO UNICO"))) {
                            wepoyPrinter.setTextSize(SMALL);
                        }
                        if (line.hasNonNull("text") && (line.get("text").asText().startsWith("FECHA EMISION"))) {
                            text = text.replace("HORA: ", "").replace("FECHA EMISION", "FECHA");
                        }

                        if (line.hasNonNull("text"))
                            wepoyPrinter.drawText(text);
                    }
                    break;
                case TEXT_COLUMN_2:
                    readAlign(line);
                    readSize(line);
                    readBold(line);


                    if(!line.get("text1").asText().isEmpty() && !line.get("text2").asText().isEmpty()) {
                        if (line.get("text1").asText().startsWith("Nombre Cliente") && !line.get("text2").asText().startsWith(" 0 0")) {
                            wepoyPrinter.drawTextLeftRight(line.get("text1").asText(), line.get("text2").asText());
                        }else if (!line.get("text1").asText().startsWith("Nombre Cliente")){
                            wepoyPrinter.drawTextLeftRight(line.get("text1").asText(), line.get("text2").asText());
                        }
                    }
                    break;
                case TEXT_COLUMN_3:
                    readAlign(line);
                    readSize(line);
                    readBold(line);

                    if(!line.get("text1").asText().isEmpty() || !line.get("text3").asText().isEmpty())
                        wepoyPrinter.drawTextLeftRight(line.get("text1").asText(), line.get("text3").asText());
                    if(!line.get("text2").asText().isEmpty())
                        wepoyPrinter.drawText(line.get("text2").asText().replace("CANTIDAD", "CANT."));

                    break;
                case TEXT_LEFT_RIGHT:
                    readAlign(line);
                    readSize(line);
                    readBold(line);

                    String textRight = " ";
                    if (line.hasNonNull("textRight"))
                        textRight = line.get("textRight").asText();

                    String textLeft = " ";
                    if (line.hasNonNull("textLeft"))
                        textLeft = line.get("textLeft").asText();

                    wepoyPrinter.drawTextLeftRight(textLeft, textRight);

                    break;
                case IMAGE_TEXT_RIGHT:
                    if (imagesJson != null && line.hasNonNull("imagen") && line.get("imagen").asText().startsWith("http") && line.hasNonNull("texts")) {
                        Bitmap image = imagesJson.get(line.get("imagen").asText());
                        if (image != null) {
                            JsonNode texts = line.get("texts");

                            wepoyPrinter.drawBitmap(fixTransparence(image));
                            wepoyPrinter.resetStyle();

                            for (int i = 0; i < texts.size(); i++) {
                                wepoyPrinter.drawText(texts.get(i).asText());
                            }
                        }
                    }

                    break;
                case IMAGE:
                    readAlign(line);
                    if (line.hasNonNull("imagen") && line.get("imagen").asText().startsWith("http")) {
                        Bitmap image = imagesJson.get(line.get("imagen").asText());
                        if (image != null) {
                            wepoyPrinter.drawBitmap(fixTransparence(image));
                        }
                    }
                    break;
                case LINE:
                    wepoyPrinter.drawLine();
                    break;
                case DOT_LINE:
                    wepoyPrinter.drawDotLine();
                    break;
                case LINE_BREAK:
                    wepoyPrinter.lineBreak();
                    break;
                case SYMBOL_PDF417_STANDARD:
                    wepoyPrinter.drawPDF417(line.get("value").asText());

                    break;
                case BARCODE_GS1_128:
                    try {
                        Bitmap image = code128(line.get("value").asText());
                        wepoyPrinter.drawBitmap(image);

                        wepoyPrinter.setTextAlign(PrintUtils.TextAlign.CENTER);
                        wepoyPrinter.drawText(line.get("value").asText());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case CUT:
                    wepoyPrinter.lineBreak();
                    wepoyPrinter.lineBreak();
                    break;
            }
        }

    }

    private void readSize(JsonNode line) {
        if (line.hasNonNull("size")) {
            try {
                wepoyPrinter.setTextSize(PrintUtils.TextSize.valueOf(line.get("size").asText()));
                return;
            } catch (Exception e) {
            }
        }
        wepoyPrinter.setTextSize(NORMAL);
    }

    private void readAlign(JsonNode line) {
        if (line.hasNonNull("align")) {
            try {
                wepoyPrinter.setTextAlign(PrintUtils.TextAlign.valueOf(line.get("align").asText().toUpperCase()));
                return;
            } catch (Exception e) {
            }
        }
        wepoyPrinter.setTextAlign(LEFT);
    }

    private void readBold(JsonNode line) {
        if (line.hasNonNull("bold") && line.get("bold").asInt() == 1) {
            wepoyPrinter.setBold(true);
        } else {
            wepoyPrinter.setBold(false);
        }
    }


}
