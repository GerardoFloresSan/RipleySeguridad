package com.example.myfirstapp.ui.activity.seguridad

import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.CloseCartResponse
import com.example.myfirstapp.ui.activity.security.WelcomeSecurityActivity
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.printerK.PrinterToTicket
import com.example.myfirstapp.utils.printerK.SearchPrinter
import com.example.myfirstapp.utils.startActivityE
import kotlinx.android.synthetic.main.activity_end_order.*

class EndOrderActivity : RipleyBaseActivity(), PrinterToTicket.IPrinterListener{

    lateinit var closeCart: CloseCartResponse
    lateinit var printer2: PrinterToTicket

    override fun getView(): Int = R.layout.activity_end_order

    override fun onCreate() {
        closeCart = intent.getSerializableExtra("extra0") as CloseCartResponse
        printer2 = SearchPrinter.searchPrinter(this@EndOrderActivity, this@EndOrderActivity)


        btn_close_all.setOnClickListener {
            RipleyApplication.closeAll()
            startActivityE(WelcomeSecurityActivity::class.java)
        }

        btn_print.setOnClickListener {
            printer2.printComprobante(closeCart)
        }

        super.onCreate()
    }

    override fun onBackPressed() {

    }

    override fun connectedPrinter() {
        toast("connectedPrinter")
    }

    override fun endPrint() {
        toast("endPrint")
    }

    override fun warning(warning: String?) {
        toast("Falta papel")
    }

    override fun error(error: String?) {
        toast("Error con la impresion" + error.toString())
    }


}