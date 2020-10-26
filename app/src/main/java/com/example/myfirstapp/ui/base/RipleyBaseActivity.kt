package com.example.myfirstapp.ui.base

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.location.Location
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
import com.example.myfirstapp.BuildConfig
import com.example.myfirstapp.R
import com.example.myfirstapp.di.Orchestrator
import com.example.myfirstapp.ui.activity.LocationStoreActivity
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.utils.Methods
import com.example.myfirstapp.utils.PapersManager
import com.example.myfirstapp.utils.getString
import com.example.myfirstapp.utils.startActivityE

@Suppress("PropertyName")
abstract class RipleyBaseActivity : BaseActivity() {
    private var dialog: MaterialDialog? = null
    private var dialogCustom : Dialog? = null
    val PERMISSION_CAMERA = 4567
    val PERMISSION_LOCATION = 5678

    protected val component by lazy { Orchestrator.presenterComponent }
    protected val locationHelper by lazy {
        RipleyApplication.locationHelper
    }
    protected val locationTrackingService by lazy {
        RipleyApplication.locationTrackingService
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace( R.id.content, fragment).commit()
    }

    protected val termsConditions by lazy {
        Methods.getParameter(65).value.toInt()
    }
    protected val politicsPersonalData by lazy {
        Methods.getParameter(107).value.toInt()
    }
    protected val maxGlobalProduct by lazy {
        if(BuildConfig.DEBUG) 1 else Methods.getParameter(122).value.toInt()
    }
    protected val maxByProduct by lazy {
        if(BuildConfig.DEBUG) 3 else Methods.getParameter(123).value.toInt()
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


        /*dialog = MaterialDialog.Builder(this)
            .customView(R.layout.dialog_loading, false)
            .cancelable(false)
            .show()*/
    }

    fun hideLoading() {
        if (dialogCustom == null) return
        dialogCustom?.dismiss()
        dialogCustom = null
        /*if (dialog == null) return
        dialog?.dismiss()
        dialog = null*/
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
    fun getCameraPermission() {
        val permissionArrays = arrayOf(Manifest.permission.CAMERA)
        if (checkPermissionsCamera()) {
            toast("Permiso otorgado --- PERMISSION_CAMERA")
        } else {
            requestPermissions(permissionArrays, PERMISSION_CAMERA)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getLocationPermission() {
        val permissionArrays = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (checkPermissionsLocation()) {
            toast("Permiso otorgado --- PERMISSION_LOCATION")
        } else {
            requestPermissions(permissionArrays, PERMISSION_LOCATION)
        }
    }

    fun checkPermissionsCamera(): Boolean {
        val permissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionsLocation(): Boolean {
        val permissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
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

    fun restart() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_close_fail_location)
        val btnAccept: AppCompatButton = dialog.findViewById<View>(R.id.btnOk) as AppCompatButton
        val dialogTitle: AppCompatTextView = dialog.findViewById<View>(R.id.lbl_dialog_title) as AppCompatTextView
        dialogTitle.text = dialogTitle.getString().replace("xxxxxx", PapersManager.subsidiary.description)
        PapersManager.subsidiary
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.window?.attributes?.windowAnimations = R.style.AppTheme_Slide
        dialog.setCancelable(false)
        dialog.show()

        btnAccept.setOnClickListener {
            dialog.dismiss()
            RipleyApplication.closeAll()
            startActivityE(LocationStoreActivity::class.java)
        }
    }

    fun distanceShort(startPoint: Location): Float {
        val endPoint = Location("locationA")//-12.0707968,-77.1002836
        endPoint.latitude = PapersManager.locationUser.latitude
        endPoint.longitude = PapersManager.locationUser.longitude

        return startPoint.distanceTo(endPoint)
    }

    fun validLocation(): Boolean {
        if(PapersManager.gpsStatus) {
            if(PapersManager.locationUser.latitude != 0.0) {
                val subsidiary = PapersManager.subsidiary
                val subsidiaryPoint = Location("locationA").apply {
                    this.latitude = subsidiary.latitude
                    this.longitude = subsidiary.longitude
                }
                val shortDistance = distanceShort(subsidiaryPoint)
                return when {
                    //(shortDistance < 500) -> {
                    (shortDistance < Methods.getParameter(120).value.toLong()) -> {
                        true
                    }//
                    else -> {
                        restart()
                        false
                    }
                }
            } else {
                toast(getString(R.string.txt_getting_location))
                return false
            }
        } else {
            toast(getString(R.string.txt_active_gps))
            return false
        }
    }

    fun toast(text: String) = Toast.makeText(this@RipleyBaseActivity, text, Toast.LENGTH_LONG).show()
}