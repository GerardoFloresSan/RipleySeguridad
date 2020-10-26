package com.example.myfirstapp.ui.activity

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.CheckPricesResponse
import com.example.myfirstapp.data.response.LocationUser
import com.example.myfirstapp.data.response.User
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.LocationTrackingService
import com.example.myfirstapp.utils.PapersManager
import com.example.myfirstapp.utils.startActivityE
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task


class LocationStoreActivity : RipleyBaseActivity() {

    val REQUEST_CHECK_CODE = 8949
    var builder: LocationSettingsRequest.Builder? = null

    override fun getView(): Int = R.layout.activity_location_store

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkPermissionsLocation()) {
            enabledDetectGps()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getLocationPermission()
            }
        }
    }

    private fun enabledDetectGps() {
        locationHelper.detectGPS { gps ->
            if(gps) {
                PapersManager.shoppingCart = CheckPricesResponse()
                PapersManager.locationUser = LocationUser()
                PapersManager.userLocal = User()
                PapersManager.gpsStatus = true
                locationTrackingService.config()
                RipleyApplication.startLocation()
                startActivityE(WelcomeActivity::class.java)
            } else {
                gpsEnabled()
            }
        }
    }

    @Suppress("SENSELESS_COMPARISON")
    override fun onDestroy() {
        try {
            locationHelper.stop()
        } catch (e: Exception) {
            Log.d("Error locationHelper", e.message.toString())
        }
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enabledDetectGps()
                } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            RipleyApplication.closeAll()
                        } else {
                            RipleyApplication.closeAll()//TODO PERMANENT REJECTED
                        }
                    }
                } else {
                    RipleyApplication.closeAll()
                }
            }
            else -> {
                val e = 0
            }
        }
    }

    private fun gpsEnabled() {
        val request : LocationRequest = LocationRequest()
            .setFastestInterval(5000)
            .setInterval(3000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        builder = LocationSettingsRequest.Builder().addLocationRequest(request)
        val result = LocationServices.getSettingsClient(this).checkLocationSettings(builder!!.build())

        result.addOnCompleteListener {task: Task<LocationSettingsResponse> -> try {
            task.getResult(ApiException::class.java)
        }  catch (e: ApiException) {
            when(e.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    try {
                        val resolvableApiException = e as ResolvableApiException
                        resolvableApiException.startResolutionForResult(this, REQUEST_CHECK_CODE)
                    } catch (ex: IntentSender.SendIntentException) {
                        ex.printStackTrace()
                    } catch (ex: ClassCastException) {
                        ex
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
                else -> {
                    e
                }
            }
        }
        }

        /* try {
            task.getResult(ApiException::class.java)
        }  catch (e: ApiException) {
            when(e.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    try {
                        val resolvableApiException = e as ResolvableApiException
                        resolvableApiException.startResolutionForResult(this, REQUEST_CHECK_CODE)
                    } catch (ex: IntentSender.SendIntentException) {
                        ex.printStackTrace()
                    } catch (ex: ClassCastException) {
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }
        }*/
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_location)
        val btnAccept: AppCompatButton = dialog.findViewById<View>(R.id.btnOk) as AppCompatButton
        val btnCancel: AppCompatButton =
            dialog.findViewById<View>(R.id.btnCancel) as AppCompatButton


        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.window?.attributes?.windowAnimations = R.style.AppTheme_Slide
        dialog.show()

        btnAccept.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getLocationPermission()
            }
        }

        btnCancel.setOnClickListener {
            RipleyApplication.closeAll()
        }
    }
}