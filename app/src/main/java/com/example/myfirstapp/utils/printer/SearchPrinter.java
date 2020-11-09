package com.example.myfirstapp.utils.printer;
import android.content.Context;

import com.example.myfirstapp.utils.printer.wepoyprinter.PrinterWepoyManager;

public class SearchPrinter {
    public static PrinterToBoleta searchPrinter(Context ctx, PrinterToBoleta.IPrinterListener listener){
        return new PrinterWepoyManager(ctx, listener);
    }
}
