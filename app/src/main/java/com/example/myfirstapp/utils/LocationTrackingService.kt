package com.example.myfirstapp.utils

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
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

        /*
        locListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                @Suppress("SENSELESS_COMPARISON")
                if (location != null) {
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,0F,locListener as LocationListener)
        val localNetworkLocation =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (localNetworkLocation != null) locationNetwork = localNetworkLocation
*/

        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)


        if (hasGps || hasNetwork) {
            if (hasGps) {
                Log.d("CodeAndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,0F, object : LocationListener {
                        override fun onLocationChanged(location: Location) {
                            Log.d("ENTRO ACÁ SEGURIDAD -->", "onLocationChanged")
                            if (location != null) {
                                Log.d("ENTRO ACÁ SEGURIDAD-->", location.toString())
                                locationGps = location
                                Log.d("CodeAndroidLocation"," GPS Latitude : " + locationGps!!.latitude)
                                Log.d("CodeAndroidLocation"," GPS Longitude : " + locationGps!!.longitude)
                                latitudeUser = location.latitude
                                longitudeUser = location.longitude
                                onGPSChanged(0, latitudeUser, longitudeUser)
                            }
                        }

                        override fun onStatusChanged(provider: String?,status: Int,extras: Bundle?) {
                            onGPSChanged(1, latitudeUser, longitudeUser)
                        }

                        override fun onProviderEnabled(provider: String) {
                            onGPSChanged(2, latitudeUser, longitudeUser)
                        }

                        override fun onProviderDisabled(provider: String) {
                            onGPSChanged(3, latitudeUser, longitudeUser)
                        }

                    })

                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                if (localGpsLocation != null)  locationGps = localGpsLocation

            }

            if (hasNetwork) {
                Log.d("CodeAndroidLocation", "hasNetwork")
                locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 50,  0F,  object : LocationListener {
                        override fun onLocationChanged(location: Location) {
                            if (location != null) {
                                locationNetwork = location
                                Log.d("CodeAndroidLocation", " Network Latitude : " + locationNetwork!!.latitude)
                                Log.d("CodeAndroidLocation"," Network Longitude : " + locationNetwork!!.longitude)
                                onGPSChanged(0, latitudeUser, longitudeUser)
                            }
                        }

                        override fun onStatusChanged(provider: String?,status: Int,extras: Bundle?) {
                            onGPSChanged(1, latitudeUser, longitudeUser)
                        }

                        override fun onProviderEnabled(provider: String) {
                            onGPSChanged(2, latitudeUser, longitudeUser)
                        }

                        override fun onProviderDisabled(provider: String) {
                            onGPSChanged(3, latitudeUser, longitudeUser)
                        }

                    })

                val localNetworkLocation =locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null) locationNetwork = localNetworkLocation
            }

            if (locationGps != null && locationNetwork != null) {
                if (locationGps!!.accuracy > locationNetwork!!.accuracy) {
                    Log.d(
                        "CodeAndroidLocation",
                        " Network Latitude : " + locationNetwork!!.latitude
                    )
                    Log.d(
                        "CodeAndroidLocation",
                        " Network Longitude : " + locationNetwork!!.longitude
                    )
                } else {
                    Log.d("CodeAndroidLocation", " GPS Latitude : " + locationGps!!.latitude)
                    Log.d("CodeAndroidLocation", " GPS Longitude : " + locationGps!!.longitude)
                }
            }
        }
         //
        else {
            Log.d("ERROR", " ERROR GPS")

        }


    }

    fun stop() {
        locListener?.let { locationManager?.removeUpdates(it) }
    }
}