package com.example.myfirstapp.ui.activity.security

import android.os.SystemClock
import android.view.View
import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.CloseCartResponse
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.ui.base.ScanBlueToothBaseActivity
import com.example.myfirstapp.utils.Methods
import com.example.myfirstapp.utils.PapersManager
import com.example.myfirstapp.utils.setColorBackground
import com.example.myfirstapp.utils.startActivityE
import kotlinx.android.synthetic.main.activity_end_order_app_activity.*

class EndOrderAppActivity : ScanBlueToothBaseActivity() {

    lateinit var closeCart: CloseCartResponse
    private var mLastClickTime: Long = 0
    var needPrint = false

    override fun getView(): Int = R.layout.activity_end_order_app_activity

    override fun onCreate() {
        super.onCreate()

        closeCart = intent.getSerializableExtra("extra0") as CloseCartResponse
        initTextPaint()
        initBlueToothScanPrint()

        needPrint = Methods.getParameter("sgVoucherInd").value == "1"

        btn_close_all.setOnClickListener {
            if (!needPrint) {
                RipleyApplication.closeAll()
                startActivityE(WelcomeSecurityActivity::class.java)
            } else {
                toast("Voucher obligatorio")
            }
        }

        //VALIDACIONES
        if (PapersManager.macPrint.isEmpty()) {
            btn_print.visibility = View.GONE
            btn_config_blue.visibility = View.VISIBLE
            btn_close_all.isEnabled = false
            btn_close_all.isClickable = false
            btn_close_all.isFocusable = false
        } else {
            val validation = (PapersManager.macPrint.isEmpty())
            btn_close_all.isEnabled = validation
            btn_close_all.isClickable = validation
            btn_close_all.isFocusable = validation
            btn_close_all.setColorBackground(
                validation,
                this,
                R.color.colorPrimary,
                R.color.colorPrimaryOpa
            )
            btn_print.visibility = View.VISIBLE
            btn_config_blue.visibility = View.GONE
        }

        btn_print.setOnClickListener {
            if (PapersManager.macPrint.isEmpty()) {

            } else {
                initPrint(PapersManager.macPrint, closeCart.clientVoucher, false)
                needPrint = true
                btn_close_all.isEnabled = true
                btn_close_all.isClickable = true
                btn_close_all.isFocusable = true
                btn_close_all.setColorBackground(
                    true,
                    this,
                    R.color.colorPrimary,
                    R.color.colorPrimaryOpa
                )
            }

        }

        text_config.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()

            showBlueToothDevice { action ->
                btn_config_blue.visibility = if (action) View.GONE else View.VISIBLE
                btn_print.visibility = if (action) View.VISIBLE else View.GONE
            }
        }


    }


    override fun onBackPressed() {

    }
}