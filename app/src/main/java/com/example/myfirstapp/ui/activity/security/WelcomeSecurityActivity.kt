package com.example.myfirstapp.ui.activity.security

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import com.example.myfirstapp.R
import com.example.myfirstapp.ui.activity.SplashActivity
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.PapersManager
import com.example.myfirstapp.utils.startActivityE
import kotlinx.android.synthetic.main.activity_welcome_seguridad.*

class WelcomeSecurityActivity : RipleyBaseActivity() {

    override fun getView(): Int = R.layout.activity_welcome_seguridad

    @SuppressLint("SetTextI18n")
    override fun onCreate() {
        super.onCreate()

        lbl_text_name_user.text = "${PapersManager.loginAccess.name} ${PapersManager.loginAccess.lastName}"
        txt_subsidiary.text = "Ripley " + PapersManager.loginAccess.subsidiaryName

        close_session.setOnClickListener {
            PapersManager.login = false
            RipleyApplication.closeAll()
            startActivityE(SplashActivity::class.java)
        }

        btn_start_shop.setOnClickListener {
            if(checkPermissionsCamera()) {
                startActivityE(ValidationActivity::class.java)
            } //
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getCameraPermission()
                }
            }
        }
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
}