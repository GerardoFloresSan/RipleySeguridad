package com.example.myfirstapp.utils.printer.wepoyprinter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.myfirstapp.R;
import com.example.myfirstapp.data.response.CloseCartResponse;
import com.example.myfirstapp.utils.Methods;
import com.example.myfirstapp.utils.printer.PrinterToBoleta;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.mbaas.baytex.api.utils.PrintUtils.TextAlign;
import cl.mbaas.baytex.api.utils.PrintUtils.LineType;
import cl.mbaas.baytex.api.utils.PrintUtils.TextSize;

import static cl.mbaas.baytex.api.utils.PrintUtils.TextAlign.LEFT;
import static cl.mbaas.baytex.api.utils.PrintUtils.TextSize.NORMAL;
import static cl.mbaas.baytex.api.utils.PrintUtils.TextSize.SMALL;
import static cl.mbaas.baytex.api.utils.PrintUtils.code128;
import static cl.mbaas.baytex.api.utils.PrintUtils.fixTransparence;


/**
 * Created by agustin on 7/19/17.
 */

public class PrinterWepoyManager extends PrinterToBoleta {

    private WepoyPrinter wepoyPrinter = new WepoyPrinter();


    public PrinterWepoyManager(Context mContext, IPrinterListener listener) {
        super(listener, mContext);
        configPrinter();
    }

    private void configPrinter() {
        wepoyPrinter.setSpeedLevel(62);
        wepoyPrinter.setGrayLevel(30);
        wepoyPrinter.setupPage(384, -1);
    }

    private boolean checkPrinterStatus() {
        int status = wepoyPrinter.getStatus();
        switch (status) {
            case 0:
                return true;
            case -1:
                getListener().error("Impresora sin papel");
                return false;
            case -2:
                getListener().error("Impresora recalentada");
                return false;
            case -3:
                getListener().error("Bajo Voltaje");
                return false;
            case -4:
                getListener().error("Impresora ocupada");
                return false;
            case -257:
                getListener().error("Error driver");
                return false;
            default:
                getListener().error("Estado de la impresora desconocido");
                return false;

        }
    }

    @Override
    public void printComprobante(CloseCartResponse comprobante) {

        if (checkPrinterStatus()) {
            try {
                String tagLogo = "http://logo";
                List<ObjectNode> lineas = new ArrayList<ObjectNode>();
                for(int l = 0; l < comprobante.getClientVoucher().size(); l++) {
                    ObjectMapper mapper = new ObjectMapper();
                    ObjectNode node2 = mapper.convertValue(comprobante.getClientVoucher().get(l), ObjectNode.class);
                    lineas.add(node2);
                }
                //List<ObjectNode> lineas = comprobante.formatJsonNodes(tagLogo);

                Map<String, Bitmap> imagesJson = new HashMap<>();
                imagesJson.put(tagLogo, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.logo_app));

                for (int l = 0; l < lineas.size(); l++) {
                    wepoyPrinter.resetStyle();
                    printJsonLine(lineas.get(l), imagesJson);
                }

                wepoyPrinter.lineBreak();
                wepoyPrinter.lineBreak();

                wepoyPrinter.printPage();

                getListener().endPrint();
            } catch (Exception e) {
                getListener().error("Ocurrio un error al imprimir");
            }
        }
    }

    @Override
    public void printBoletaJson(final JsonNode jsonNode, final Map<String, Bitmap> imagesJson, final long numTickets) {
        if (checkPrinterStatus()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (jsonNode.hasNonNull("lineas")) {

                            List<JsonNode> ticketCambio = new ArrayList<>();
                            List<JsonNode> ofertasData = new ArrayList<>();
                            boolean haveCut = false;

                            for (int i = 0; i < jsonNode.get("lineas").size(); i++) {
                                wepoyPrinter.resetStyle();

                                JsonNode linea = jsonNode.get("lineas").get(i);

                                printJsonLine(linea, imagesJson);
                                if (linea.hasNonNull("text") && (linea.get("text").asText().startsWith("SUCURSAL") || linea.get("text").asText().startsWith("FECHA EMISION"))) {
                                    ticketCambio.add(linea);
                                    if (ofertasData.size() < 3)// puede venir el ticket de cambio
                                        ofertasData.add(0, linea);
                                }
                                if (linea.hasNonNull("text") && linea.get("text").asText().startsWith("NRO TRANSACCION")) {
                                    ofertasData.add(linea);
                                }
                                if (linea.hasNonNull("tipo") && linea.get("tipo").asText().equalsIgnoreCase("BARCODE_GS1_128")) {
                                    ticketCambio.add(linea);
                                }
                                if (linea.hasNonNull("tipo") && linea.get("tipo").asText().equalsIgnoreCase("CUT")) {
                                    haveCut = true;
                                }
                            }

                            if (numTickets > 0) {
                                wepoyPrinter.drawCut();
                                printTicketCambio(jsonNode, numTickets, false);
                                wepoyPrinter.lineBreak();
                            } else {
                                wepoyPrinter.lineBreak();
                                wepoyPrinter.lineBreak();
                            }
                        }

                        wepoyPrinter.printPage();

                        getListener().endPrint();
                    } catch (Exception e) {
                        getListener().error("Ocurrio un error al imprimir");
                    }
                }
            }).start();

        }
    }

    public void printTicketCambio(JsonNode jsonNode, long num) {
        printTicketCambio(jsonNode, num, true);
    }

    private void printTicketCambio(JsonNode jsonNode, long num, boolean print) {
        if (checkPrinterStatus()) {
            try {
                try {

                    if (print) {
                        wepoyPrinter.lineBreak();
                        wepoyPrinter.lineBreak();
                    }

                    if (jsonNode.hasNonNull("lineas")) {

                        List<JsonNode> ticketCambio = new ArrayList<>();

                        for (int i = 0; i < jsonNode.get("lineas").size(); i++) {
                            JsonNode linea = jsonNode.get("lineas").get(i);
                            if (linea.hasNonNull("text") && (linea.get("text").asText().startsWith("SUCURSAL") || linea.get("text").asText().startsWith("FECHA EMISION"))) {
                                ticketCambio.add(linea);
                            }
                            if (linea.hasNonNull("tipo") && linea.get("tipo").asText().equalsIgnoreCase("BARCODE_GS1_128")) {
                                ticketCambio.add(linea);
                            }
                        }

                        if (ticketCambio.size() == 3) {
                            for (int i = 0; i < num; i++) {
                                wepoyPrinter.lineBreak();

                                wepoyPrinter.setTextAlign(TextAlign.CENTER);
                                wepoyPrinter.drawText("TICKET DE CAMBIO");
                                wepoyPrinter.lineBreak();

                                for (int l = 0; l < ticketCambio.size(); l++) {
                                    wepoyPrinter.resetStyle();
                                    JsonNode linea = ticketCambio.get(l);
                                    if (linea.hasNonNull("tipo") && linea.get("tipo").asText().equalsIgnoreCase("BARCODE_GS1_128")) {
                                        wepoyPrinter.lineBreak();
                                    }
                                    printJsonLine(linea, null);
                                }

                                wepoyPrinter.lineBreak();
                                wepoyPrinter.lineBreak();

                                if (i + 1 != num)
                                    wepoyPrinter.drawCut();


                            }

                            if (print)
                                wepoyPrinter.printPage();
                        }
                    }
                } catch (Exception e) {
                    getListener().error("Error al imprimir la boleta");
                }
                getListener().endPrint();
            } catch (Exception e) {
                getListener().error("Ocurrio un error al imprimir");
            }
        }
    }

    @Override
    public void connect() {
        getListener().connectedPrinter();
    }

    @Override
    public void disconnect() {
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

                    if(line.get("text1") != null && line.get("text2") != null) {
                        if(!line.get("text1").asText().isEmpty() && !line.get("text2").asText().isEmpty()) {
                            if (line.get("text1").asText().startsWith("Nombre Cliente") && !line.get("text2").asText().startsWith(" 0 0")) {
                                wepoyPrinter.drawTextLeftRight(line.get("text1").asText(), line.get("text2").asText());
                            } else if (!line.get("text1").asText().startsWith("Nombre Cliente")){
                                wepoyPrinter.drawTextLeftRight(line.get("text1").asText(), line.get("text2").asText());
                            }
                        }
                    }
                    break;
                case TEXT_COLUMN_3:
                    readAlign(line);
                    readSize(line);
                    readBold(line);

                    if(line.get("text1") != null && line.get("text3") != null) {
                        if(!line.get("text1").asText().isEmpty() || !line.get("text3").asText().isEmpty()) wepoyPrinter.drawTextLeftRight(line.get("text1").asText(), line.get("text3").asText());
                    }

                    if(line.get("text2") != null) {
                        if(!line.get("text2").asText().isEmpty()) wepoyPrinter.drawText(line.get("text2").asText().replace("CANTIDAD", "CANT."));
                    }

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

                        wepoyPrinter.setTextAlign(TextAlign.CENTER);
                        wepoyPrinter.drawText(line.get("value").asText());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case CUT:
                    wepoyPrinter.lineBreak();
                    wepoyPrinter.lineBreak();
                    break;

                default:
                    if(line.get("tipo") != null) {
                        if(line.get("tipo").asText().equals("CODE_TEXT_QR")) {
                            try {
                                Bitmap image = Methods.Companion.generateQRCode(line.get("value").asText(), 220, 220);
                                wepoyPrinter.drawBitmap(image);

                                wepoyPrinter.setTextAlign(TextAlign.CENTER);
                                wepoyPrinter.drawText(line.get("value").asText());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;

            }
        }

    }

    private void readSize(JsonNode line) {
        if (line.hasNonNull("size")) {
            try {
                wepoyPrinter.setTextSize(TextSize.valueOf(line.get("size").asText()));
                return;
            } catch (Exception e) {
            }
        }
        wepoyPrinter.setTextSize(NORMAL);
    }

    private void readAlign(JsonNode line) {
        if (line.hasNonNull("align")) {
            try {
                wepoyPrinter.setTextAlign(TextAlign.valueOf(line.get("align").asText().toUpperCase()));
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
