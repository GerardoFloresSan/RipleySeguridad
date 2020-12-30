package com.example.myfirstapp.ui.base

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import android.text.Spanned
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.example.myfirstapp.R
import com.example.myfirstapp.di.Orchestrator
import com.example.myfirstapp.ui.activity.SplashActivity
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.utils.PapersManager
import com.example.myfirstapp.utils.startActivityE

abstract class RipleyBaseActivity : BaseActivity() {
    private var dialog: MaterialDialog? = null
    private var dialogCustom : Dialog? = null
    @Suppress("PropertyName")
    val PERMISSION_CAMERA = 4567
    @Suppress("PropertyName")
    val PERMISSION_WRITE = 7489
    @Suppress("PropertyName")
    val PERMISSION_READ = 3389

    protected val component by lazy { Orchestrator.presenterComponent }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace( R.id.content, fragment).commit()
    }

    fun getContext() = this

    fun showLoading() {
        hideLoading()
        dialogCustom = Dialog(this)
        dialogCustom!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogCustom!!.setContentView(R.layout.dialog_loading)
        dialogCustom!!.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogCustom!!.window?.setBackgroundDrawableResource(android.R.color.transparent);
        dialogCustom!!.window?.attributes?.windowAnimations = R.style.AppTheme_Slide
        dialogCustom!!.setCancelable(false)
        dialogCustom!!.show()
    }

    fun hideLoading() {
        if (dialogCustom == null) return
        dialogCustom?.dismiss()
        dialogCustom = null
    }

    //Todo crear dos dialogs

    fun getErrorDialog(message: String) = MaterialDialog.Builder(this)
        .title(getString(R.string.txt_alert_dialog_error))
        .content(message)
        .positiveText(getString(R.string.txt_ok_dialog))

    fun showError(message: String) {
        getErrorDialog(message).show()
    }

    fun showError(message: Int) {
        showError(getString(message))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getWritePermission() {
        val permissionArrays = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (checkPermissionsCamera()) {
            toast("Permiso otorgado --- PERMISSION_CAMERA")
        } else {
            requestPermissions(permissionArrays, PERMISSION_WRITE)
        }
    }

    fun checkPermissionsWrite(): Boolean {
        val permissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getReadPermission() {
        val permissionArrays = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (checkPermissionsRead()) {
            toast("Permiso otorgado --- PERMISSION_READ")
        } else {
            requestPermissions(permissionArrays, PERMISSION_READ)
        }
    }

    fun checkPermissionsRead(): Boolean {
        val permissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getCameraPermission() {
        val permissionArrays = arrayOf(Manifest.permission.CAMERA)
        if (checkPermissionsCamera()) {
            toast("Permiso otorgado --- PERMISSION_CAMERA")
        } else {
            requestPermissions(permissionArrays, PERMISSION_CAMERA)
        }
    }

    fun checkPermissionsCamera(): Boolean {
        val permissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    fun customDialog(text: String, returnAction: (Boolean) -> Unit) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_location)
        val txtLabel: AppCompatTextView = dialog.findViewById<View>(R.id.lbl_dialog_title) as AppCompatTextView
        val btnAccept: AppCompatButton = dialog.findViewById<View>(R.id.btnOk) as AppCompatButton
        val btnCancel: AppCompatButton = dialog.findViewById<View>(R.id.btnCancel) as AppCompatButton
        txtLabel.text = text

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.window?.attributes?.windowAnimations = R.style.AppTheme_Slide
        dialog.setCancelable(false)
        dialog.show()

        btnCancel.setOnClickListener {
            returnAction(false)
            dialog.dismiss()
        }

        btnAccept.setOnClickListener {
            returnAction(true)
            dialog.dismiss()
        }
    }

    fun customInfo(text: Spanned, returnAction: (Boolean) -> Unit) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_information)
        val txtLabel: AppCompatTextView = dialog.findViewById<View>(R.id.lbl_dialog_info) as AppCompatTextView
        val btnAccept: AppCompatImageButton = dialog.findViewById<View>(R.id.btn_close) as AppCompatImageButton
        txtLabel.text = text

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.window?.attributes?.windowAnimations = R.style.AppTheme_Slide
        dialog.setCancelable(false)
        dialog.show()

        btnAccept.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun tokenExpired() {
        dialog = getErrorDialog("Su sesión ha expirado.")
            .cancelable(false)
            .onPositive { _, _ ->
                PapersManager.login = false
                RipleyApplication.closeAll()
                startActivityE(SplashActivity::class.java)
            }.show()
    }

    fun toast(text: String) = Toast.makeText(this@RipleyBaseActivity, text, Toast.LENGTH_LONG).show()
}