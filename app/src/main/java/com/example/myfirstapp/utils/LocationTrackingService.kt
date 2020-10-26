package com.example.myfirstapp.utils

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

data class LocationTrackingService(val context: Context) {

    private lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    var latitudeUser: Double = 0.0
    var longitudeUser: Double = 0.0
    var locationGps: Location? = null
    private var locationNetwork: Location? = null
    private var locListener: LocationListener? = null

    fun config() {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    @SuppressLint("MissingPermission")
    fun locationDetector(onGPSChanged: (Int, Double, Double) -> Unit) {
        locListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                @Suppress("SENSELESS_COMPARISON")
                if(location != null) {
                    latitudeUser = location.latitude
                    longitudeUser = location.longitude
                    onGPSChanged(0, latitudeUser, longitudeUser)
                }
            }

            override fun onProviderDisabled(provider: String) {
                onGPSChanged(1, latitudeUser, longitudeUser)
            }

            override fun onProviderEnabled(provider: String) {
                onGPSChanged(2, latitudeUser, longitudeUser)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                onGPSChanged(3, latitudeUser, longitudeUser)
            }
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, locListener as LocationListener)
        val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (localNetworkLocation != null) locationNetwork = localNetworkLocation
    }

    fun stop() {
        locListener?.let { locationManager?.removeUpdates(it) }
    }
}