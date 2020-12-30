package com.example.myfirstapp.ui.activity.security

import android.os.SystemClock
import android.util.Log
import android.view.View
import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.CloseCartResponse
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.ui.base.PdfBaseActivity
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.ui.base.ScanBlueToothBaseActivity
import com.example.myfirstapp.utils.PapersManager
import com.example.myfirstapp.utils.ProcessBitmap
import com.example.myfirstapp.utils.startActivityE
import kotlinx.android.synthetic.main.activity_end_order.*
import kotlinx.android.synthetic.main.activity_welcome_seguridad.*

class EndOrderAppActivity : ScanBlueToothBaseActivity() {

    lateinit var closeCart: CloseCartResponse

    private var mLastClickTime: Long = 0

    override fun getView(): Int = R.layout.activity_end_order

    /*private lateinit var list: MutableList<CloseCartResponse.ClientVoucher>*/

    override fun onCreate() {
        closeCart = intent.getSerializableExtra("extra0") as CloseCartResponse

        initTextPaint()
        initBlueToothScanPrint()

        btn_close_all.setOnClickListener {
            RipleyApplication.closeAll()
            startActivityE(WelcomeSecurityActivity::class.java)
        }


        btn_print.setOnClickListener {
            initPrint(PapersManager.macPrint, closeCart.clientVoucher)
        }

        btn_print_2.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()

            showBlueToothDevice{ action ->
                btn_print_2.visibility = if(action)View.GONE else View.INVISIBLE

            }


        }

        super.onCreate()
    }



    override fun onBackPressed() {

    }
}