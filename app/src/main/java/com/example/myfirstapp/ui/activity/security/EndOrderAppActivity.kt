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
        /*val featuresJsonEntity = jsonAdapter.fromJson("{\"features\": [     {       \"text\": \"RIPLEY\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"TIENDAS POR DEPARTAMENTO RIPLEY S.A.\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"CALLE LAS BEGONIAS 545-577\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"SAN ISIDRO - LIMA\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"RUC 20337564373\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"CONSTANCIA DE EMISIÓN DEL\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"COMPROBANTE ELECTRÓNICO\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"text\": \"20024/899 08/11/20 13:07\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"74354 VENDEDOR SUCUR: 20024\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"text1\": \"Doc Cliente :\",       \"text2\": \" DNI 77177123\",       \"sizeColumn1\": \"13\",       \"sizeColumn3\": \"10\",       \"tipo\": \"TEXT_COLUMN_2\"     },     {       \"text1\": \"Nombre Cliente :\",       \"text2\": \" Gerardo Flores\",       \"sizeColumn1\": \"13\",       \"sizeColumn3\": \"10\",       \"tipo\": \"TEXT_COLUMN_2\"     },     {       \"text1\": \"eMail :\",       \"text2\": \" gerardo.gabriel.flores@gmail.com\",       \"sizeColumn1\": \"13\",       \"sizeColumn3\": \"10\",       \"tipo\": \"TEXT_COLUMN_2\"     },     {       \"textLeft\": \"2015250306817\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"POL MC DEGLOBO INX C\",       \"textRight\": \"179.85\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"3 X 59.95\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"2030237401712\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"FLORERO PEQUEO OCRE \",       \"textRight\": \"99.95\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"1 X 99.95\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"2044234672075\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"BOLSA REGULAR.      \",       \"textRight\": \"1.25\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"5 X 0.25\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"textLeft\": \"SUBTOTAL S/\",       \"textRight\": \"160.15\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"text\": \"NRO DE UNIDADES: 9\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"textLeft\": \"TOTAL A PAGAR S/\",       \"textRight\": \"160.15\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"textLeft\": \"MASTERCARD\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"525435****7677\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"text\": \"ORDEN DE COMPRA: 14770282\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"value\": \"ee9818b027bcd29f0cdf880ee11729ed416723ab3894d446434d144d3ce6837e451df3a74059c989016dc34cb9ffc78e148bfbde64e270cee16c6ad4cdd5d376\",       \"tipo\": \"CODE_TEXT_QR\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"text\": \"Tu boleta electronica sera enviada a\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"gerardo.gabriel.flores@gmail.com\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"CUT\"     }   ]}")

        list = featuresJsonEntity?.features as MutableList<CloseCartResponse.ClientVoucher>*/


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