package com.example.myfirstapp.ui.activity.security

import android.os.SystemClock
import android.view.View
import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.CloseCartResponse
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.Methods
import com.example.myfirstapp.utils.printerK.PrinterToTicket
import com.example.myfirstapp.utils.printerK.SearchPrinter
import com.example.myfirstapp.utils.setColorBackground
import com.example.myfirstapp.utils.startActivityE
import kotlinx.android.synthetic.main.activity_end_order.*

class EndOrderActivity : RipleyBaseActivity(), PrinterToTicket.IPrinterListener {

    lateinit var closeCart: CloseCartResponse
    lateinit var printer2: PrinterToTicket
    var needPrint = false
    private var mLastClickTime: Long = 0

    override fun getView(): Int = R.layout.activity_end_order

    override fun onCreate() {
        closeCart = intent.getSerializableExtra("extra0") as CloseCartResponse
        printer2 = SearchPrinter.searchPrinter(this@EndOrderActivity, this@EndOrderActivity)


        btn_close_all.setOnClickListener {
            if (!needPrint) {
                RipleyApplication.closeAll()
                startActivityE(WelcomeSecurityActivity::class.java)
            } else {
                toast("Voucher obligatorio")
            }
        }

        btn_print.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            showLoading()
            needPrint = true
            validButton()
            printer2.printComprobante(closeCart)
        }

        needPrint = Methods.getParameter("sgVoucherInd").value == "1"

        validButton()
        super.onCreate()
    }

    override fun onBackPressed() {

    }

    private fun validButton() {
        btn_close_all.isEnabled = !needPrint
        btn_close_all.isClickable = !needPrint
        btn_close_all.isFocusable = !needPrint
        btn_close_all.setColorBackground(
            !needPrint,
            this,
            R.color.colorPrimary,
            R.color.colorPrimaryOpa
        )
    }


    override fun connectedPrinter() {
        //hideLoading()
        toast("connectedPrinter")
    }

    override fun endPrint() {
        hideLoading()
        needPrint = false
        validButton()
        btn_print.visibility = View.GONE
        toast("Fin de impresión")
    }

    override fun warning(warning: String?) {
        hideLoading()
        needPrint = false
        validButton()
        toast("Falta papel")
    }

    override fun error(error: String?) {
        hideLoading()
        needPrint = false
        validButton()
        toast("Error con la impresion" + error.toString())
    }


}