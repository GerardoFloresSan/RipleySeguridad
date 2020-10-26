package com.example.myfirstapp.ui.activity

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.core.content.ContextCompat
import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.Subsidiary
import com.example.myfirstapp.presenter.SubsidiaryPresenter
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.Methods
import com.example.myfirstapp.utils.PapersManager
import com.example.myfirstapp.utils.startActivityE
import kotlinx.android.synthetic.main.activity_welcome.*
import java.io.Serializable
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule


class WelcomeActivity : RipleyBaseActivity(), SubsidiaryPresenter.View {

    @Inject
    lateinit var subsidiaryPresenter: SubsidiaryPresenter

    override fun getView(): Int = R.layout.activity_welcome

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_welcome)
        component.inject(this)
        subsidiaryPresenter.attachView(this)
        showLoading()
        timeStart()
    }

    private fun timeStart() {
        Timer().schedule(1000) {
            runOnUiThread {
                if (PapersManager.locationUser.latitude != 0.0) {
                    subsidiaryPresenter.getSubsidiary()
                } else {
                    timeStart()
                }
            }
        }
    }

    @Suppress("SENSELESS_COMPARISON")
    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        subsidiaryPresenter.attachView(this)
        super.onResume()
    }

    override fun onPause() {
        subsidiaryPresenter.detachView()
        super.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    RipleyApplication.closeAll()
                    startActivityE(ScanActivity::class.java)
                } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            toast("Camera ------ Never ask again PERMISSION_DENIED")
                        }
                    }*/
                    toast("Que es obligatorio el permiso de la cámara para poder continuar")
                }
            }
            else -> {
                toast("Que es obligatorio el permiso de la cámara para poder continuar")
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        RipleyApplication.closeAll()
    }

    fun obtenerNombreDeDispositivo(): String? {
        val fabricante = Build.MANUFACTURER
        val modelo = Build.MODEL
        return if (modelo.startsWith(fabricante)) {
            primeraLetraMayuscula(modelo)
        } else {
            primeraLetraMayuscula(fabricante) + " " + modelo
        }
    }


    private fun primeraLetraMayuscula(cadena: String?): String {
        if (cadena == null || cadena.length == 0) {
            return ""
        }
        val primeraLetra = cadena[0]
        return if (Character.isUpperCase(primeraLetra)) {
            cadena
        } else {
            Character.toUpperCase(primeraLetra).toString() + cadena.substring(1)
        }
    }

    override fun subsidiarySuccessPresenter(status: Int, vararg args: Serializable) {
        when (status) {
            200 -> {
                var short: Long = 100000000000000
                var subsidiary: Subsidiary = Subsidiary()
                @Suppress("UseExpressionBody", "UNCHECKED_CAST")
                for (element in (args[0] as ArrayList<Subsidiary>)) {
                    val startPoint = Location("locationA")//-12.0775301,-77.0827161
                    startPoint.latitude = element.latitude
                    startPoint.longitude = element.longitude
                    val short2 = distanceShort(startPoint).toLong()

                    if (short2 < short) {
                        short = short2
                        subsidiary = element
                    }
                }
                @Suppress("UseExpressionBody")
                when {
                    //(short < 500) -> {
                    (short < Methods.getParameter(120).value.toLong()) -> {
                        descriptionStore.text = "Usted se encuentra en la tienda:"
                        titleStore.text = "Ripley " + subsidiary.description

                        descriptionStore.visibility = View.VISIBLE
                        titleStore.visibility = View.VISIBLE
                        btnValidationLocation.visibility = View.INVISIBLE
                        txtCloseApp.visibility = View.INVISIBLE
                        btnStartShop.isEnabled = true
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            btnStartShop.backgroundTintList =
                                ColorStateList.valueOf(getColor(R.color.colorPrimary))
                        }//
                        else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                btnStartShop.background.setTint(
                                    ContextCompat.getColor(
                                        this,
                                        R.color.colorPrimary
                                    )
                                )
                            }
                        }
                        btnStartShop.setOnClickListener {
                            PapersManager.subsidiary = subsidiary
                            /*
                            val analytics = FirebaseAnalytics.getInstance(this)
                            val bundle = Bundle()
                            bundle.putString(
                                "usernew",
                                "Modelo del celular" + obtenerNombreDeDispositivo()
                            )
                            analytics.logEvent("Usuario Nuevo", bundle)*/
                            //Todo validacion de Paper
                            if (PapersManager.newUsers) {
                                PapersManager.newUsers = false
                                RipleyApplication.closeAll()
                                startActivityE(OnBoardingActivity::class.java)
                            }//
                            else {
                                if (checkPermissionsCamera()) {
                                    RipleyApplication.closeAll()
                                    startActivityE(ScanActivity::class.java)
                                } else {
                                    getCameraPermission()
                                }
                            }

                        }
                    }
                    (short in Methods.getParameter(120).value.toLong()..Methods.getParameter(121).value.toLong()) -> {
                        descriptionStore.text =
                            "Lo sentimos, te encuentras fuera del rango. Acércate a la tienda de ${subsidiary.description} para iniciar tu compra."
                        descriptionStore.visibility = View.VISIBLE
                        titleStore.visibility = View.INVISIBLE
                        btnValidationLocation.visibility = View.VISIBLE
                        txtCloseApp.visibility = View.INVISIBLE
                        btnStartShop.isEnabled = false
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            btnStartShop.backgroundTintList =
                                ColorStateList.valueOf(getColor(R.color.colorBloc))
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                btnStartShop.background.setTint(
                                    ContextCompat.getColor(
                                        this,
                                        R.color.colorBloc
                                    )
                                )
                            }
                        }
                        btnValidationLocation.setOnClickListener {
                            showLoading()
                            subsidiaryPresenter.getSubsidiary()
                        }
                    }
                    else -> {
                        descriptionStore.text =
                            "Lo sentimos, todavía no tenemos\n cobertura para tu ubicación."

                        descriptionStore.visibility = View.VISIBLE
                        titleStore.visibility = View.INVISIBLE
                        btnValidationLocation.visibility = View.INVISIBLE
                        txtCloseApp.visibility = View.VISIBLE
                        btnStartShop.isEnabled = false
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            btnStartShop.backgroundTintList =
                                ColorStateList.valueOf(getColor(R.color.colorBloc))
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                btnStartShop.background.setTint(
                                    ContextCompat.getColor(
                                        this,
                                        R.color.colorBloc
                                    )
                                )
                            }
                        }
                        txtCloseApp.setOnClickListener {
                            RipleyApplication.closeAll()
                        }
                    }
                }
            }
            204 -> {
                descriptionStore.text = ""
                // toast("204---------------")
            }
            else -> {
                // toast("Otro---------------")
            }
        }

    }
}