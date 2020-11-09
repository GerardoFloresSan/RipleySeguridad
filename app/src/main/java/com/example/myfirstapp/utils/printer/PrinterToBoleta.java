package com.example.myfirstapp.utils.printer;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.myfirstapp.data.response.CloseCartResponse;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;

public abstract class PrinterToBoleta {

    public interface IPrinterListener{
        void connectedPrinter();
        void endPrint();
        void warning(String warning);
        void error(String error);
    }

    private IPrinterListener listener;
    protected Context mContext;

    public abstract void printComprobante(CloseCartResponse comprobante);

    public abstract void printBoletaJson(JsonNode jsonNode, Map<String, Bitmap> imagesJson, long numTickets);

    public abstract void printTicketCambio(JsonNode boletaJson, long num);

    public PrinterToBoleta(IPrinterListener listener, Context mContext){
        super();
        this.listener = listener;
        this.mContext = mContext;
    }

    public IPrinterListener getListener() {
        return listener;
    }

    public void setListener(IPrinterListener listener) {
        this.listener = listener;
    }

    public abstract void connect();

    public abstract void disconnect();

    public void downloadImagesAndPrintBoletaJson(final JsonNode boletaJson, final long numTickets){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    printBoletaJson(boletaJson, null, numTickets);
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
