package com.example.myfirstapp.ui.application

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.util.Log
import com.example.myfirstapp.di.componet.AppComponent
import com.example.myfirstapp.di.componet.DaggerAppComponent
import com.example.myfirstapp.di.module.AppModule
import com.example.myfirstapp.utils.LocationTrackingService
import com.example.myfirstapp.utils.Methods
import com.example.myfirstapp.utils.PapersManager
import io.paperdb.Paper
import java.lang.Exception
import java.util.*

open class RipleyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Paper.init(this)
        Methods.init(this)

        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        /*locationHelper = LocationSimpleTracker(this)*/
        locationTrackingService = LocationTrackingService(this)
    }

    @SuppressLint("StaticFieldLeak")
    companion object {
        lateinit var appComponent: AppComponent
        /*lateinit var locationHelper: LocationSimpleTracker*/

        @SuppressLint("StaticFieldLeak")
        lateinit var locationTrackingService: LocationTrackingService

        private val activities = Stack<Activity>()


        fun startLocation() {
            locationTrackingService.locationDetector { status, latitude, longitude ->
                when(status) {
                    0 -> {
                   /*  PapersManager.locationUser = LocationUser().apply {
                            this.latitude = latitude
                            this.longitude = longitude
                        }*/
                        Log.d("locationTrackingService", "------------0")
                    }
                    1 -> {
                        //PapersManager.locationUser = LocationUser()
                        PapersManager.gpsStatus = false
                        Log.d("locationTrackingService", "------------1")
                    }
                    2 -> {
                        PapersManager.gpsStatus = true
                        Log.d("locationTrackingService", "------------2")
                    }
                    3 -> {
                        Log.d("locationTrackingService", "------------3")
                    }
                }
            }
        }

        fun exitsActivities() : Boolean {
            return activities.isEmpty()
        }

        fun addActivity(activity: Activity) {
            activities.add(activity)
        }

        fun removeActivity(activity: Activity) {
            activities.remove(activity)
        }

        fun closeAll() {
            for (activity in activities) {
                try {
                    activity.finish()
                } catch (ignore: Exception) {
                }
            }
            activities.clear()
        }
    }

}