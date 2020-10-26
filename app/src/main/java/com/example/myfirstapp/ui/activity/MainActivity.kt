package com.example.myfirstapp.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapp.R
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.google.android.gms.location.LocationSettingsRequest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : RipleyBaseActivity() {

    val REQUEST_CHECK_CODE = 8949
    var builder: LocationSettingsRequest.Builder? = null

    override fun getView(): Int = R.layout.activity_main

    override fun onCreate() {
        setTheme(R.style.AppTheme)
        super.onCreate()
        val startPoint = Location("locationA")//-12.0775301,-77.0827161 Mi casa
        startPoint.latitude = -11.9927565
        startPoint.longitude = -77.0657342

        val endPoint = Location("locationB")//-12.0707968,-77.1002836 Mega plaza
        endPoint.latitude = -11.9955609
        endPoint.longitude = -77.0637384

        val distance: Float = startPoint.distanceTo(endPoint)
        example.text = "$distance"

        locationHelper.detectGPS { gps ->
            if (gps)
                toast("locationA ------ true")
            else
                toast("locationA ------ false")
        }

        /*val request : LocationRequest = LocationRequest()
            .setFastestInterval(5000)
            .setInterval(3000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        builder = LocationSettingsRequest.Builder().addLocationRequest(request)

        val result = LocationServices.getSettingsClient(this).checkLocationSettings(builder!!.build())

        result.addOnCompleteListener {task: Task<LocationSettingsResponse> -> try {
                task.getResult(ApiException::class.java)

                toast("Permiso 1 --- location")
            }  catch (e: ApiException) {
                when(e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            val resolvableApiException = e as ResolvableApiException
                            resolvableApiException.startResolutionForResult(this, REQUEST_CHECK_CODE)
                            toast("Permiso 0 --- location")
                        } catch (ex: IntentSender.SendIntentException) {
                            toast("Permiso 1 --- location")
                            ex.printStackTrace()
                        } catch (ex: ClassCastException) {
                            toast("Permiso 2 --- location")
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        toast("Permiso 3 --- location")
                    }
                }
            }
        }*/
        camera_id.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getCameraPermission()
            }
        }
        location_id.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getLocationPermission()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toast("Camera ------ PERMISSION_GRANTED")
                } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            toast("Camera ------ Never ask again PERMISSION_DENIED")
                        }
                    }
                    toast("Camera ------ PERMISSION_DENIED")
                } else {
                    toast("Camera ------ OTHER")
                }
            }
            PERMISSION_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toast("Location ------ PERMISSION_GRANTED")
                } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            RipleyApplication.closeAll()
                        }
                    }
                    RipleyApplication.closeAll()
                } else {
                    RipleyApplication.closeAll()
                }
            }
            REQUEST_CHECK_CODE -> {
                toast("Location ------ OTHER")
            }
            else -> {
            }
        }
    }

}