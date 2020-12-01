package com.example.myfirstapp.ui.activity.security

import android.content.pm.PackageManager
import android.os.Build
import android.text.Editable
import android.view.View
import com.example.myfirstapp.BuildConfig
import com.example.myfirstapp.R
import com.example.myfirstapp.data.request.LoginRequest
import com.example.myfirstapp.data.response.CloseCartResponse
import com.example.myfirstapp.presenter.UserPresenter
import com.example.myfirstapp.ui.base.PdfBaseActivity
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.synthetic.main.activity_login.*
import java.io.Serializable
import javax.inject.Inject


class LoginActivity : RipleyBaseActivity(), UserPresenter.View {

    @Inject
    lateinit var userPresenter: UserPresenter

    private lateinit var list: MutableList<CloseCartResponse.ClientVoucher>

    override fun getView(): Int = R.layout.activity_login

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        userPresenter.attachView(this)
        txt_input_user.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                cleanError(txt_input_user)
            }
        })

        txt_input_password.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                cleanError(txt_input_password)
            }
        })

        btnStartShop.setOnClickListener {
            if(checkPermissionsRead()) {
                userPresenter.login(LoginRequest().apply {
                    this.username = txt_input_user.getString()
                    this.password = txt_input_password.getString()
                    this.version = BuildConfig.VERSION_NAME
                    this.session = Methods.generateRandomString()
                })
            } //
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getReadPermission()
                }
            }
        }

        /*val jsonAdapter by lazy {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

            moshi.adapter<FeaturesJsonEntity>(FeaturesJsonEntity::class.java)
        }

        val featuresJsonEntity = jsonAdapter.fromJson("{\"features\": [     {       \"text\": \"RIPLEY\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"TIENDAS POR DEPARTAMENTO RIPLEY S.A.\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"CALLE LAS BEGONIAS 545-577\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"SAN ISIDRO - LIMA\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"RUC 20337564373\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"CONSTANCIA DE EMISIÓN DEL\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"COMPROBANTE ELECTRÓNICO\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"text\": \"20024/899 08/11/20 13:07\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"74354 VENDEDOR SUCUR: 20024\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"text1\": \"Doc Cliente :\",       \"text2\": \" DNI 77177123\",       \"sizeColumn1\": \"13\",       \"sizeColumn3\": \"10\",       \"tipo\": \"TEXT_COLUMN_2\"     },     {       \"text1\": \"Nombre Cliente :\",       \"text2\": \" Gerardo Flores\",       \"sizeColumn1\": \"13\",       \"sizeColumn3\": \"10\",       \"tipo\": \"TEXT_COLUMN_2\"     },     {       \"text1\": \"eMail :\",       \"text2\": \" gerardo.gabriel.flores@gmail.com\",       \"sizeColumn1\": \"13\",       \"sizeColumn3\": \"10\",       \"tipo\": \"TEXT_COLUMN_2\"     },     {       \"textLeft\": \"2015250306817\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"POL MC DEGLOBO INX C\",       \"textRight\": \"179.85\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"3 X 59.95\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"2030237401712\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"FLORERO PEQUEO OCRE \",       \"textRight\": \"99.95\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"1 X 99.95\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"2044234672075\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"BOLSA REGULAR.      \",       \"textRight\": \"1.25\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"5 X 0.25\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"textLeft\": \"SUBTOTAL S/\",       \"textRight\": \"160.15\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"text\": \"NRO DE UNIDADES: 9\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"textLeft\": \"TOTAL A PAGAR S/\",       \"textRight\": \"160.15\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"textLeft\": \"MASTERCARD\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"525435****7677\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"text\": \"ORDEN DE COMPRA: 14770282\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"value\": \"ee9818b027bcd29f0cdf880ee11729ed416723ab3894d446434d144d3ce6837e451df3a74059c989016dc34cb9ffc78e148bfbde64e270cee16c6ad4cdd5d376\",       \"tipo\": \"CODE_TEXT_QR\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"text\": \"Tu boleta electronica sera enviada a\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"gerardo.gabriel.flores@gmail.com\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"CUT\"     }   ]}")

        list = featuresJsonEntity?.features as MutableList<CloseCartResponse.ClientVoucher>

        val bitmap = generateBitmap(list as ArrayList<CloseCartResponse.ClientVoucher>)

        btnStartShop.setOnClickListener {
            ProcessBitmap(object : ProcessBitmap.DoStuff {
                override fun getContext() = this@LoginActivity
                @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
                override fun done(filePath: String) {
                }
            }).execute(bitmap)
        }*/

        /*Glide.with(this).load(bitmap)
            .into(imageView)*/
    }

    /*data class FeaturesJsonEntity(val features: List<CloseCartResponse.ClientVoucher>)*/

    override fun onResume() {
        userPresenter.attachView(this)
        super.onResume()
    }

    override fun onPause() {
        userPresenter.detachView()
        super.onPause()
    }

    fun cleanError(viewBackground: View) {
        txt_error.visibility = View.INVISIBLE
        viewBackground.setBackgroundResource(R.drawable.shape_input_text)
        validButton()
    }

    private fun validButton() {
        val validation =
            (txt_input_user.getString().isNotEmpty() && txt_input_password.getString().isNotEmpty())
        btnStartShop.isEnabled = validation
        btnStartShop.isClickable = validation
        btnStartShop.isFocusable = validation
        btnStartShop.setColorBackground(
            validation,
            this,
            R.color.colorPrimary,
            R.color.colorPrimaryOpa
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            PERMISSION_READ ->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    userPresenter.login(LoginRequest().apply {
                        this.username = txt_input_user.getString()
                        this.password = txt_input_password.getString()
                        this.version = BuildConfig.VERSION_NAME
                        this.session = Methods.generateRandomString()
                    })
                } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    toast("Que es obligatorio el permiso de la escritura para poder continuar")
                }
            }
            else -> {
                toast("Que es obligatorio el permiso de la escritura para poder continuar")
            }
        }
    }

    override fun userSuccessPresenter(status: Int, vararg args: Serializable) {
        when (status) {
            200 -> {
                finish()
                PapersManager.username = txt_input_user.getString()
                startActivityE(WelcomeSecurityActivity::class.java)
            }
            401 -> {
                txt_error.visibility = View.VISIBLE
                txt_input_user.setBackgroundResource(R.drawable.shape_text_error)
                txt_input_password.setBackgroundResource(R.drawable.shape_text_error)
            }
            666 -> {
                showError(args[0] as String)
            }
            else -> {
                txt_error.visibility = View.VISIBLE
                txt_input_user.setBackgroundResource(R.drawable.shape_text_error)
                txt_input_password.setBackgroundResource(R.drawable.shape_text_error)
            }
        }
    }


}