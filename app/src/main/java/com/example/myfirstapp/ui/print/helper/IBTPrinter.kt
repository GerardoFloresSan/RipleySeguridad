package com.example.myfirstapp.ui.print.helper

import android.content.Context
import android.graphics.Bitmap
import io.reactivex.Observable

interface IBTPrinter {
    fun disconect()

    fun connect(mac: String, context: Context): Observable<Boolean>

    fun printBitmap(bitmap: Bitmap): Observable<Int>
}