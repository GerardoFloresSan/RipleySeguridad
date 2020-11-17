package com.example.myfirstapp.ui.activity.security

import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.CloseCartResponse
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.Methods
import com.example.myfirstapp.utils.printerK.PrinterToTicket
import com.example.myfirstapp.utils.printerK.SearchPrinter
import com.example.myfirstapp.utils.startActivityE
import kotlinx.android.synthetic.main.activity_end_order.*

class EndOrderActivity : RipleyBaseActivity(), PrinterToTicket.IPrinterListener {

    lateinit var closeCart: CloseCartResponse
    lateinit var printer2: PrinterToTicket
    var needPrint = false

    override fun getView(): Int = R.layout.activity_end_order

    override fun onCreate() {
        closeCart = intent.getSerializableExtra("extra0") as CloseCartResponse
        printer2 = SearchPrinter.searchPrinter(this@EndOrderActivity, this@EndOrderActivity)


        btn_close_all.setOnClickListener {
            if(!needPrint) {
                RipleyApplication.closeAll()
                startActivityE(WelcomeSecurityActivity::class.java)
            } else {
                toast("Voucher obligatorio")
            }
        }

        btn_print.setOnClickListener {
            //showLoading()
            needPrint = true
            printer2.printComprobante(closeCart)
        }

        needPrint = Methods.getParameter("sgVoucherInd").value == "1"

        super.onCreate()
    }

    override fun onBackPressed() {

    }

    override fun connectedPrinter() {
        //hideLoading()
        needPrint = false
        toast("connectedPrinter")
    }

    override fun endPrint() {
        //hideLoading()
        needPrint = false
        toast("endPrint")
    }

    override fun warning(warning: String?) {
        //hideLoading()
        needPrint = false
        toast("Falta papel")
    }

    override fun error(error: String?) {
        //hideLoading()
        needPrint = false
        toast("Error con la impresion" + error.toString())
    }


}