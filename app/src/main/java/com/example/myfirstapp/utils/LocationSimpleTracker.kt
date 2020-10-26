package com.example.myfirstapp.utils

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.*

data class LocationSimpleTracker(val context: Context) {
    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    fun detectGPS(onGPSChanged: (Boolean) -> Unit) {
        locationCallback = object : LocationCallback() {

            override fun onLocationAvailability(var1: LocationAvailability?) {
                onGPSChanged(var1?.isLocationAvailable ?: false)
            }

            override fun onLocationResult(result: LocationResult?) {

            }
        }
        fusedLocationProviderClient.requestLocationUpdates(
            buildLocationRequest(),
            locationCallback,
            null
        )
    }

    private fun buildLocationRequest(): LocationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 5000 //5 seconds
        fastestInterval = 5000 //5 seconds
        maxWaitTime = 1000 //1 seconds
    }

    fun stop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}