package com.example.myfirstapp.ui.print.helper

import android.graphics.Bitmap
import io.reactivex.Observable
import pe.com.viergegroup.rompefilassdk.print.BaseBtPrinter

open class BaseBTPrinter : IBTPrinter {

    override fun disconect() {
        TODO("Not yet implemented")
    }

    override fun connect(mac: String): Observable<Boolean> {
        TODO("Not yet implemented")
    }

    override fun printBitmap(bitmap: Bitmap): Observable<Int> {
        TODO("Not yet implemented")
    }

    fun <T> printTransaction(voucher: Bitmap, t: T, print: BaseBtPrinter.Printer<T>) {
        print.print(voucher, t)
        print.onEnd(t)
    }
}