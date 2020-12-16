package ripley.integracion.visanet.helper

import android.graphics.Bitmap
import io.reactivex.Observable
import pe.com.viergegroup.rompefilassdk.database.Database
import pe.com.viergegroup.rompefilassdk.database.InitializationDatabase
import pe.com.viergegroup.rompefilassdk.database.TechnicalMenuDatabase
import pe.com.viergegroup.rompefilassdk.models.PrintVoucherModel
import pe.com.viergegroup.rompefilassdk.print.BaseBtPrinter
import pe.com.viergegroup.rompefilassdk.print.VoucherGenerator
import pe.com.viergegroup.rompefilassdk.print.VoucherType
import pe.com.viergegroup.rompefilassdk.util.Common

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