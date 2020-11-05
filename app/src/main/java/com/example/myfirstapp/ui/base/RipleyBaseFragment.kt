package com.example.myfirstapp.ui.base

import android.content.Context
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.example.myfirstapp.R
import com.example.myfirstapp.di.Orchestrator
import com.example.myfirstapp.ui.activity.SplashActivity
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.utils.startActivityE

abstract class RipleyBaseFragment : BaseFragment() {

    private var dialog: MaterialDialog? = null

    override fun getContext(): Context = this.activity?.applicationContext!!

    protected val component by lazy { Orchestrator.presenterComponent }

    fun showLoading() {
        hideLoading()
        dialog = MaterialDialog.Builder(activity!!)
            .title("Conectando...")
            .content("Espera un momento")
            .progress(true, 0)
            .cancelable(false)
            .show()
    }

    fun hideLoading() {
        if (dialog == null) return
        dialog?.dismiss()
        dialog = null
    }

    fun showError(message: String) {
        MaterialDialog.Builder(this.activity!!)
            .title("Advertencia")
            .content(message)
            .positiveText("Ok")
            .show()
    }

    fun replaceFragment(fragment: Fragment) {
        fragmentManager!!.beginTransaction().replace(R.id.content, fragment).commit()
    }

    fun showError(message: Int) {
        showError(getString(message))
    }

    fun getErrorDialog(message: String) = MaterialDialog.Builder(this.activity!!)
        .title(getString(R.string.txt_alert_dialog_error))
        .content(message)
        .positiveText(getString(R.string.txt_ok_dialog))

    fun tokenExpired() {
        dialog = getErrorDialog("Su sesión ha expirado.")
            .onPositive { _, _ ->
                RipleyApplication.closeAll()
                startActivityE(SplashActivity::class.java)
            }.show()
    }
}