package com.example.myfirstapp.ui.print.helper

import android.graphics.Bitmap
import android.text.TextUtils
import android.util.Log
import com.example.myfirstapp.ui.print.helper.BaseBTPrinter
import com.pax.gl.commhelper.ICommBt
import com.pax.gl.commhelper.exception.CommException
import com.pax.gl.commhelper.impl.PaxGLComm
import com.pax.gl.extprinter.entity.BitmapLine
import com.pax.gl.extprinter.entity.EAlign
import com.pax.gl.extprinter.impl.GLExtPrinter
import com.pax.gl.extprinter.inf.ICommListener
import com.pax.gl.extprinter.inf.IExtPrinter
import io.reactivex.Observable
import pe.com.viergegroup.rompefilassdk.pax.pay.app.FinancialApplication
import pe.com.viergegroup.rompefilassdk.print.BaseBtPrinter

class BTPrinter : BaseBTPrinter() {

    private val _tag = BTPrinter::class.java.simpleName

    private var _comm: ICommBt? = null

    override fun disconect() {
        try {
            _comm!!.disconnect()
        } catch (e: Exception) {
            Log.e(_tag, "No se pudo cerrar la conexion.")
        }
    }

    private val _btListener: ICommListener = object : ICommListener {
        @Throws(com.pax.gl.extprinter.exception.CommException::class)
        override fun onSend(data: ByteArray) {
            if (_comm != null) {
                try {
                    _comm!!.send(data)
                } catch (e: CommException) {
                    e.printStackTrace()
                    throw com.pax.gl.extprinter.exception.CommException(-2)
                }
            } else {
                Log.e(_tag, "comm is null, send error")
            }
        }

        @Throws(com.pax.gl.extprinter.exception.CommException::class)
        override fun onRecv(i: Int): ByteArray {
            var data = ByteArray(0)
            if (_comm != null) {
                data = try {
                    _comm!!.recv(1)
                } catch (e: CommException) {
                    e.printStackTrace()
                    throw com.pax.gl.extprinter.exception.CommException(-3)
                }
            }
            return data
        }

        override fun onReset() {
            _comm?.reset() ?: Log.e(_tag, "comm is null, reset error")
        }
    }

    override fun connect(mac: String): Observable<Boolean> {
        return Observable.create { it ->
            if (TextUtils.isEmpty(mac)) {
                it.onNext(false)
                it.onComplete()
            } else {
                _comm = PaxGLComm.getInstance(FinancialApplication.getAppContext()).createBt(mac)

                var result = try {
                    _comm?.reset()
                    _comm?.connect()
                    true
                } catch (e: Exception) {
                    false
                }

                it.onNext(result)
                it.onComplete()
            }
        }
    }

    override fun printBitmap(bitmap: Bitmap): Observable<Int> {
        val escPos = GLExtPrinter.createEscPosPrinter(_btListener, 383)
        return Observable.create { emitter ->
            this.printTransaction(
                bitmap,
                escPos,
                object : BaseBtPrinter.Printer<IExtPrinter> {
                    override fun print(bitmap: Bitmap, iExtPrinter: IExtPrinter) {
                        iExtPrinter.reset()
                        iExtPrinter.print(BitmapLine(bitmap, EAlign.CENTER))
                    }

                    override fun onEnd(iExtPrinter: IExtPrinter) {
                        iExtPrinter.cutPaper(1)
                        emitter.onNext(0)
                        emitter.onComplete()
                    }
                })
        }
    }

}