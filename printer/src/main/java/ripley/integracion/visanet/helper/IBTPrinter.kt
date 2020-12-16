package ripley.integracion.visanet.helper

import android.graphics.Bitmap
import io.reactivex.Observable

interface IBTPrinter {
    fun disconect()

    fun connect(mac: String): Observable<Boolean>

    fun printBitmap(bitmap: Bitmap): Observable<Int>
}