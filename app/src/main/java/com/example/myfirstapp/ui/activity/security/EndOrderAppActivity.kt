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

        btn_print.visibility = View.GONE

        btn_print.setOnClickListener {
            //TODO OPCION 1 DE PREFERENCIA QUE LA MAC DE LA IMPRESORA SEA OTORGADA POR EL LOGIN DEL USUARIO 1 CELULAR 1 IMPRESORA ASIGNADA
            //TODO OPCION 2 EL USUARIO SETEA LA MAC MANUALMENTE
            initPrint("68:9E:19:17:8A:85", closeCart.clientVoucher)
        }

        btn_print_2.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()

            val bitmap = generateBitmap(closeCart.clientVoucher)
            ProcessBitmap(object : ProcessBitmap.DoStuff {
                override fun getContext() = this@EndOrderAppActivity

                @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
                override fun done(filePath: String) {
                    Log.d("IMAGE", "-------------------------$filePath")
                    toast("Imagen guardada")
                }
            }).execute(bitmap)

        }

        super.onCreate()
    }

    override fun onBackPressed() {

    }
}