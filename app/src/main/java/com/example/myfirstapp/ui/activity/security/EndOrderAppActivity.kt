package com.example.myfirstapp.ui.activity.security

import android.os.SystemClock
import android.util.Log
import android.view.View
import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.CloseCartResponse
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.ui.base.PdfBaseActivity
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.ProcessBitmap
import com.example.myfirstapp.utils.startActivityE
import kotlinx.android.synthetic.main.activity_end_order.*

class EndOrderAppActivity : PdfBaseActivity() {

    lateinit var closeCart: CloseCartResponse

    private var mLastClickTime: Long = 0

    override fun getView(): Int = R.layout.activity_end_order

    /*private lateinit var list: MutableList<CloseCartResponse.ClientVoucher>*/

    override fun onCreate() {
        closeCart = intent.getSerializableExtra("extra0") as CloseCartResponse
        initTextPaint()

        btn_close_all.setOnClickListener {
            RipleyApplication.closeAll()
            startActivityE(WelcomeSecurityActivity::class.java)
        }

        btn_print_2.setOnClickListener {
            //TODO
            /**
             * Abrir un pop up que tenga la lista de las impresoras (Botones - Guardar - Probar)
             *
             *
             * **/
        }

        btn_print.setOnClickListener {
            //TODO abrir un popup o una lista -- BL --
            initPrint("68:9E:19:17:8A:85", closeCart.clientVoucher)
        }

        super.onCreate()
    }

    override fun onBackPressed() { }

}