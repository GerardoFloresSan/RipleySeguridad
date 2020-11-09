package com.example.myfirstapp.utils.printerK

import android.content.Context
import android.graphics.Bitmap
import com.example.myfirstapp.data.response.CloseCartResponse
import com.fasterxml.jackson.databind.JsonNode

abstract class PrinterToTicket(var listener: IPrinterListener, protected var mContext: Context) {

    abstract fun printComprobante(comprobante: CloseCartResponse?)

    abstract fun printBoletaJson(jsonNode: JsonNode, imagesJson: Map<String, Bitmap>?, numTickets: Long)

    abstract fun printTicketCambio(boletaJson: JsonNode, num: Long)

    abstract fun connect()

    abstract fun disconnect()

    fun downloadImagesAndPrintBoletaJson(boletaJson: JsonNode, numTickets: Long) {
        try {
            Thread(Runnable { printBoletaJson(boletaJson, null, numTickets) }).start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface IPrinterListener {
        fun connectedPrinter()
        fun endPrint()
        fun warning(warning: String?)
        fun error(error: String?)
    }
}