package com.example.myfirstapp.ui.print

import android.content.Context
import androidx.multidex.MultiDex
import pe.com.viergegroup.rompefilassdk.pax.pay.app.FinancialApplication

class MainApp: FinancialApplication() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}