package ripley.integracion.visanet

import android.content.Context
import android.support.multidex.MultiDex
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