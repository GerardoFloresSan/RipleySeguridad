package com.example.myfirstapp.utils.printerK

import android.content.Context
import com.example.myfirstapp.utils.printerK.wepoyprinter.PrinterRipleyManager

object SearchPrinter {
    fun searchPrinter(ctx: Context, listener: PrinterToTicket.IPrinterListener): PrinterToTicket {
        return PrinterRipleyManager(ctx, listener)
    }
}