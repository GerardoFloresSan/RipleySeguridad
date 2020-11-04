package com.example.myfirstapp.ui.application

import android.app.Activity
import android.app.Application
import com.example.myfirstapp.di.componet.AppComponent
import com.example.myfirstapp.di.componet.DaggerAppComponent
import com.example.myfirstapp.di.module.AppModule
import com.example.myfirstapp.utils.Methods
import io.paperdb.Paper
import java.lang.Exception
import java.util.*

open class RipleyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Paper.init(this)
        Methods.init(this)

        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    companion object {
        lateinit var appComponent: AppComponent

        private val activities = Stack<Activity>()

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