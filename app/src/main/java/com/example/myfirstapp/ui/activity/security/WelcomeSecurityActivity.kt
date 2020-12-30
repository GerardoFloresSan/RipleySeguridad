package com.example.myfirstapp.ui.activity.security

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import com.example.myfirstapp.R
import com.example.myfirstapp.presenter.ParameterPresenter
import com.example.myfirstapp.presenter.UserPresenter
import com.example.myfirstapp.ui.activity.SplashActivity
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.ui.base.ScanBlueToothBaseActivity
import com.example.myfirstapp.utils.Methods
import com.example.myfirstapp.utils.PapersManager
import com.example.myfirstapp.utils.startActivityE
import kotlinx.android.synthetic.main.activity_welcome_seguridad.*
import java.io.Serializable
import java.text.SimpleDateFormat
import javax.inject.Inject

class WelcomeSecurityActivity : ScanBlueToothBaseActivity(), ParameterPresenter.View {

    @Inject
    lateinit var parameterPresenter: ParameterPresenter

    override fun getView(): Int = R.layout.activity_welcome_seguridad

    @SuppressLint("SetTextI18n")
    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        parameterPresenter.attachView(this)
        parameterPresenter.getParameters()

        lbl_text_name_user.text = "${PapersManager.loginAccess.name} ${PapersManager.loginAccess.lastName}"
        txt_subsidiary.text = "Ripley " + PapersManager.loginAccess.subsidiaryName

        initBlueToothScanPrint()
        //6 get_bluetooth.visibility = View.VISIBLE
        get_bluetooth.setOnClickListener {
            showBlueToothDevice{ action ->
                get_bluetooth.visibility = if(action)View.GONE else View.VISIBLE
            }
        }

        close_session.setOnClickListener {
            PapersManager.login = false
            RipleyApplication.closeAll()
            startActivityE(SplashActivity::class.java)
        }

        btn_start_shop.setOnClickListener {
            if(PapersManager.parametersAll.isNotEmpty()) {
                if(PapersManager.device.contains(Methods.getNameModelDevice()!!.toLowerCase())) {
                    startActivityE(ScanQrActivity::class.java)
                } else {
                    if(checkPermissionsCamera()) {
                        startActivityE(ValidationActivity::class.java)
                    } //
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            getCameraPermission()
                        }
                    }
                }
            } else {
                toast("Obteniendo parametros por favor espere")
            }
        }
    }

    override fun onResume() {
        parameterPresenter.attachView(this)
        super.onResume()
    }

    override fun onPause() {
        parameterPresenter.detachView()
        super.onPause()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            PERMISSION_CAMERA ->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityE(ValidationActivity::class.java)
                } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    toast("Que es obligatorio el permiso de la cámara para poder continuar")
                }
            }
            else -> {
                toast("Que es obligatorio el permiso de la cámara para poder continuar")
            }
        }
    }

    override fun onBackPressed() {

    }

    override fun parameterSuccessPresenter(status: Int, vararg args: Serializable) {
    }
}