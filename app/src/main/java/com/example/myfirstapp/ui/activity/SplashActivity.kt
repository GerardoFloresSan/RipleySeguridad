package com.example.myfirstapp.ui.activity

import android.os.Bundle
import android.os.Handler
import com.example.myfirstapp.R
import com.example.myfirstapp.ui.activity.security.LoginActivity
import com.example.myfirstapp.ui.activity.security.WelcomeSecurityActivity
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.PapersManager
import com.example.myfirstapp.utils.startActivityE

class SplashActivity : RipleyBaseActivity() {

    override fun getView(): Int = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({
            if (PapersManager.login) startActivityE(WelcomeSecurityActivity::class.java) else startActivityE(LoginActivity::class.java)
        }, 3000)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }


}