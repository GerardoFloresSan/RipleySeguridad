package com.example.myfirstapp.ui.activity.security

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.text.Editable
import android.util.Log
import android.view.View
import com.example.myfirstapp.BuildConfig
import com.example.myfirstapp.R
import com.example.myfirstapp.data.request.LoginRequest
import com.example.myfirstapp.presenter.UserPresenter
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.ui.base.ScanBlueToothBaseActivity
import com.example.myfirstapp.utils.*
import kotlinx.android.synthetic.main.activity_login.*
import java.io.Serializable
import javax.inject.Inject


class LoginActivity : ScanBlueToothBaseActivity(), UserPresenter.View {

    @Inject
    lateinit var userPresenter: UserPresenter

    override fun getView(): Int = R.layout.activity_login

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        userPresenter.attachView(this)

        txt_version_seg.text = "v1.0.0-34"

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
            enabledBluetooth()
            if (checkAll()) {
                enabledDetectGps()
            } //
            else {
                getPermission()
            }
        }
    }

    private fun enabledDetectGps() {
        removeGPS()
        locationHelper.detectGPS { gps ->
            if (gps) {
                loginP()
            } else {
                gpsEnabled()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeGPS()
    }

    private fun loginP() {
        userPresenter.login(LoginRequest().apply {
            this.username = txt_input_user.getString()
            this.password = txt_input_password.getString()
            this.version = BuildConfig.VERSION_NAME
            this.session = Methods.generateRandomString()
        })
    }

    private fun getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                !checkPermissionsLocation() -> getLocationPermission()
                !checkPermissionsRead() -> getReadPermission()
                !checkPermissionsCamera() -> getCameraPermission()
            }
        }
    }

    override fun onResume() {
        //txt_input_user.setText("0020024")
        //txt_input_password.setText("Ripley20024")
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_READ -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkAll()) {
                        enabledDetectGps()
                    } else {
                        getPermission()
                    }
                } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    toast("Que es obligatorio el permiso de la escritura para poder continuar")
                }
            }
            PERMISSION_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkAll()) {
                        enabledDetectGps()
                    } else {
                        getPermission()
                    }
                } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    toast("Que es obligatorio el permiso de la escritura para poder continuar")
                }
            }
            PERMISSION_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkAll()) {
                        enabledDetectGps()
                    } else {
                        getPermission()
                    }
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
                PapersManager.macPrint = ""
                PapersManager.macPrint2 = ""
                PapersManager.username = txt_input_user.getString()
                locationTrackingService.config()
                RipleyApplication.startLocation()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    loginP()
                    Log.d("result ok", data.toString())
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    toast("Es necesario activar el GPS para el funcionamiento del app")
                }
            }
            else -> {
                if (data != null) {
                    var a = data.getStringExtra("ScannerResponse")
                    if (a != null) {
                        Log.d("PrintTesttttt", a)
                    }
                }
            }
        }
    }


}


/*fun gg() {

    val jsonAdapter by lazy {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        moshi.adapter<FeaturesJsonEntity>(FeaturesJsonEntity::class.java)
    }

    val featuresJsonEntity = jsonAdapter.fromJson("{\"features\": [     {       \"text\": \"RIPLEY\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"TIENDAS POR DEPARTAMENTO RIPLEY S.A.\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"CALLE LAS BEGONIAS 545-577\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"SAN ISIDRO - LIMA\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"RUC 20337564373\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"CONSTANCIA DE EMISIÓN DEL\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"COMPROBANTE ELECTRÓNICO\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"text\": \"20024/899 08/11/20 13:07\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"74354 VENDEDOR SUCUR: 20024\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"text1\": \"Doc Cliente :\",       \"text2\": \" DNI 77177123\",       \"sizeColumn1\": \"13\",       \"sizeColumn3\": \"10\",       \"tipo\": \"TEXT_COLUMN_2\"     },     {       \"text1\": \"Nombre Cliente :\",       \"text2\": \" Gerardo Flores\",       \"sizeColumn1\": \"13\",       \"sizeColumn3\": \"10\",       \"tipo\": \"TEXT_COLUMN_2\"     },     {       \"text1\": \"eMail :\",       \"text2\": \" gerardo.gabriel.flores@gmail.com\",       \"sizeColumn1\": \"13\",       \"sizeColumn3\": \"10\",       \"tipo\": \"TEXT_COLUMN_2\"     },     {       \"textLeft\": \"2015250306817\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"POL MC DEGLOBO INX C\",       \"textRight\": \"179.85\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"3 X 59.95\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"2030237401712\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"FLORERO PEQUEO OCRE \",       \"textRight\": \"99.95\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"1 X 99.95\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"2044234672075\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"BOLSA REGULAR.      \",       \"textRight\": \"1.25\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"5 X 0.25\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"textLeft\": \"SUBTOTAL S/\",       \"textRight\": \"160.15\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"text\": \"NRO DE UNIDADES: 9\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"textLeft\": \"TOTAL A PAGAR S/\",       \"textRight\": \"160.15\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"textLeft\": \"MASTERCARD\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"textLeft\": \"525435****7677\",       \"textRight\": \"\",       \"tipo\": \"TEXT_LEFT_RIGHT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"text\": \"ORDEN DE COMPRA: 14770282\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"value\": \"ee9818b027bcd29f0cdf880ee11729ed416723ab3894d446434d144d3ce6837e451df3a74059c989016dc34cb9ffc78e148bfbde64e270cee16c6ad4cdd5d376\",       \"tipo\": \"CODE_TEXT_QR\"     },     {       \"tipo\": \"LINE_BREAK\"     },     {       \"text\": \"Tu boleta electronica sera enviada a\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"text\": \"gerardo.gabriel.flores@gmail.com\",       \"align\": \"CENTER\",       \"tipo\": \"TEXT\"     },     {       \"tipo\": \"CUT\"     }   ]}")

    list = featuresJsonEntity?.features as MutableList<CloseCartResponse.ClientVoucher>
    var intent = Intent(this, PrintActivity::class.java)
        //intent.setClassName("ripley.integracion.visanet", "ripley.integracion.visanet.PrintActivity")
        intent.setAction("ripley.intent.action.INIT_PRINT")
        intent.putExtra("list", list as ArrayList)
        intent.putExtra("mac", "68:9E:19:17:8A:85")
        intent.putExtra("print", "{\"mac\":\"68:9E:19:17:8A:85\",\"ticket\":[{\"text\":\"RIPLEY\",\"align\":\"CENTER\",\"tipo\":\"TEXT\"},{\"text\":\"TIENDAS POR DEPARTAMENTO RIPLEY S.A.\",\"align\":\"CENTER\",\"tipo\":\"TEXT\"},{\"text\":\"CALLE LAS BEGONIAS 545-577\",\"align\":\"CENTER\",\"tipo\":\"TEXT\"},{\"text\":\"SAN ISIDRO - LIMA\",\"align\":\"CENTER\",\"tipo\":\"TEXT\"},{\"text\":\"RUC 20337564373\",\"align\":\"CENTER\",\"tipo\":\"TEXT\"},{\"text\":\"BP89-60003913\",\"align\":\"CENTER\",\"tipo\":\"TEXT\"},{\"text\":\"CONSTANCIA DE EMISIÓN DEL\",\"align\":\"CENTER\",\"tipo\":\"TEXT\"},{\"text\":\"COMPROBANTE ELECTRÓNICO\",\"align\":\"CENTER\",\"tipo\":\"TEXT\"},{\"tipo\":\"LINE_BREAK\"},{\"value\":\"141220000898050005\",\"tipo\":\"BARCODE_GS1_128\"},{\"tipo\":\"LINE_BREAK\"},{\"text\":\"00089/805 14/12/20 15:53 0005\",\"tipo\":\"TEXT\"},{\"text\":\"0000231633 VENDEDOR SUCUR: 89\",\"tipo\":\"TEXT\"},{\"text\":\"SERIE: K3DF035915 01 VENTA\",\"tipo\":\"TEXT\"},{\"text\":\"DIRECCION: AV. LOS ANGELES N° 602 TIENDA TD-2, C.C. MALL PLAZA\",\"tipo\":\"TEXT\"},{\"text\":\"DISTRITO: COMAS - LIMA\",\"tipo\":\"TEXT\"},{\"tipo\":\"LINE_BREAK\"},{\"text1\":\"TICKET NRO :\",\"text2\":\" 0005\",\"sizeColumn1\":\"13\",\"sizeColumn3\":\"10\",\"tipo\":\"TEXT_COLUMN_2\"},{\"text1\":\"Doc Cliente :\",\"text2\":\" DNI 41038615\",\"sizeColumn1\":\"13\",\"sizeColumn3\":\"10\",\"tipo\":\"TEXT_COLUMN_2\"},{\"text1\":\"Nombre Cliente :\",\"text2\":\" Estanislao Chavez\",\"sizeColumn1\":\"13\",\"sizeColumn3\":\"10\",\"tipo\":\"TEXT_COLUMN_2\"},{\"text1\":\"eMail :\",\"text2\":\" EMSAPB@GMAIL.COM\",\"sizeColumn1\":\"13\",\"sizeColumn3\":\"10\",\"tipo\":\"TEXT_COLUMN_2\"},{\"tipo\":\"LINE_BREAK\"},{\"textLeft\":\"2020235119641\",\"textRight\":\"\",\"tipo\":\"TEXT_LEFT_RIGHT\"},{\"textLeft\":\"SHO UB TRAINING 829T\",\"textRight\":\"69.90\",\"tipo\":\"TEXT_LEFT_RIGHT\"},{\"text\":\"1 X 69.90\",\"tipo\":\"TEXT\"},{\"textLeft\":\"2005256597333\",\"textRight\":\"\",\"tipo\":\"TEXT_LEFT_RIGHT\"},{\"textLeft\":\"PANTALON ST PC-52201\",\"textRight\":\"138.60\",\"tipo\":\"TEXT_LEFT_RIGHT\"},{\"text\":\"1 X 138.60\",\"tipo\":\"TEXT\"},{\"tipo\":\"LINE_BREAK\"},{\"textLeft\":\"SUBTOTAL S/\",\"textRight\":\"208.50\",\"tipo\":\"TEXT_LEFT_RIGHT\"},{\"text\":\"NRO DE UNIDADES: 2\",\"tipo\":\"TEXT\"},{\"tipo\":\"LINE_BREAK\"},{\"textLeft\":\"OP. GRAVADAS\",\"textRight\":\"176.69\",\"tipo\":\"TEXT_LEFT_RIGHT\"},{\"textLeft\":\"I.G.V. (18%)\",\"textRight\":\"31.81\",\"tipo\":\"TEXT_LEFT_RIGHT\"},{\"textLeft\":\"TOTAL A PAGAR S/\",\"textRight\":\"208.50\",\"tipo\":\"TEXT_LEFT_RIGHT\"},{\"text\":\"SON: DOSCIENTOS OCHO Y 50/100 SOLES\",\"bold\":\"1\",\"tipo\":\"TEXT\"},{\"tipo\":\"LINE_BREAK\"},{\"textLeft\":\"VISA\",\"textRight\":\"\",\"tipo\":\"TEXT_LEFT_RIGHT\"},{\"textLeft\":\"421355******9077\",\"textRight\":\"208.50\",\"tipo\":\"TEXT_LEFT_RIGHT\"},{\"tipo\":\"LINE_BREAK\"},{\"tipo\":\"LINE_BREAK\"},{\"text\":\"ORDEN DE COMPRA: 32419024\",\"tipo\":\"TEXT\"},{\"tipo\":\"LINE_BREAK\"},{\"text\":\"SI TIENE TC RIPLEY ACTIVA, ESTA COMPRA\",\"align\":\"CENTER\",\"tipo\":\"TEXT\"},{\"text\":\"GANA RIPLEYPUNTOS GO.ABONO 15 MES SGTE\",\"align\":\"CENTER\",\"tipo\":\"TEXT\"},{\"tipo\":\"LINE_BREAK\"},{\"text\":\"PARA CONSULTAR TUS VALES\",\"align\":\"CENTER\",\"tipo\":\"TEXT\"},{\"text\":\"GANADOS, INGRESA CON TU DNI A\",\"align\":\"CENTER\",\"tipo\":\"TEXT\"},{\"text\":\"usatuvale.ripley.com.pe\",\"align\":\"CENTER\",\"tipo\":\"TEXT\"},{\"tipo\":\"CUT\"}]}")
        this.startActivityForResult(intent, 2000)
}*/