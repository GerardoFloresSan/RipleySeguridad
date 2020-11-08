package com.example.myfirstapp.ui.activity.seguridad

import android.device.PrinterManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.CloseCartResponse
import com.example.myfirstapp.data.response.SalesGetByResponse
import com.example.myfirstapp.ui.activity.SplashActivity
import com.example.myfirstapp.ui.activity.security.WelcomeSecurityActivity
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.startActivityE
import kotlinx.android.synthetic.main.activity_end_order.*

class EndOrderActivity : RipleyBaseActivity() {

    lateinit var closeCart: CloseCartResponse

    lateinit var printerManager: PrinterManager

    override fun getView(): Int  = R.layout.activity_end_order

    override fun onCreate() {
        closeCart = intent.getSerializableExtra("extra0") as CloseCartResponse
        btn_close_all.setOnClickListener {
            RipleyApplication.closeAll()
            startActivityE(WelcomeSecurityActivity::class.java)
        }
        btn_print.setOnClickListener {

        }

        printerManager = PrinterManager()

        super.onCreate()
    }

    override fun onBackPressed() {

    }
}