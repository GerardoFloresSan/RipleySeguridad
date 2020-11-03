package com.example.myfirstapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Point
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Methods(private val context: Context) {

    private fun getPoint(): Point {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay

        val size = Point()
        display.getSize(size)

        return size
    }

    fun toPixels(dpi: Float): Float {
        val d = context.resources.displayMetrics.density
        return dpi * d
    }

    fun getWidthScreen(): Int {
        return getPoint().x
    }

    fun getHeightScreen(): Int {
        return getPoint().y
    }

    @SuppressLint("NewApi")
    fun isInternetConnected(): Boolean {
        var isConnected = false
        val cm = methods?.context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    isConnected = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        isConnected = this.isConnectedOrConnecting
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        isConnected = this.isConnectedOrConnecting
                    }
                }
            }
        }
        Log.d("tag-connected", isConnected.toString())
        return isConnected
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var methods: Methods? = null

        fun init(context: Context) {
            methods = Methods(context)
        }

        @SuppressLint("NewApi")
        fun isInternetConnected(): Boolean {
            var isConnected = false
            val cm = methods?.context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        isConnected = when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                            else -> false
                        }
                    }
                }
            } else {
                cm.run {
                    cm.activeNetworkInfo?.run {
                        if (type == ConnectivityManager.TYPE_WIFI) {
                            isConnected = this.isConnectedOrConnecting
                        } else if (type == ConnectivityManager.TYPE_MOBILE) {
                            isConnected = this.isConnectedOrConnecting
                        }
                    }
                }
            }
            Log.d("tag-connected", isConnected.toString())
            return isConnected
        }

        fun getWidthScreen(): Int {
            return methods?.getWidthScreen() ?: 0
        }

        fun getHeightScreen(): Int {
            return methods?.getHeightScreen() ?: 0
        }

        fun toPixels(dpi: Float): Float {
            return methods?.toPixels(dpi) ?: 0f
        }

        fun getRandom(min: Int, max: Int): Int {
            var min = min
            var max = max
            max = Math.pow(10.0, max.toDouble()).toInt()
            min = Math.pow(10.0, min.toDouble()).toInt()
            return Random().nextInt(max - min + 1) + min
        }

        fun ifString(s: String): Int {
            return try {
                if (s.toInt() == -1) 88 else s.toInt()
            } catch (e: NumberFormatException) {
                88
            }
        }

        @Suppress("LocalVariableName")
        fun formatMoney(number: Double) : String {
            val COUNTRY = "PE"
            val LANGUAGE = "es"
            return NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(number).replace("/", "/ ")
        }

        fun setColorBackground(validation: Boolean, btn: View, context: Context, colorTrue: Int, colorFalse: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btn.backgroundTintList =
                    if (validation) ColorStateList.valueOf(context.getColor(colorTrue)) else ColorStateList.valueOf(
                        context.getColor(colorFalse)
                    )
            }
            else {
                btn.background.setTint(
                    ContextCompat.getColor(
                        context,
                        if (validation) colorTrue else colorFalse
                    )
                )
            }
        }

        fun getTime(time: Long): String {

            val minutes: Int = TimeUnit.MINUTES.convert(Date().time - time, TimeUnit.MILLISECONDS).toInt()

            return when {
                minutes == 0 -> "ahora"
                minutes < 60 -> "hace $minutes min"
                minutes < (60 * 24) -> "hace " + (minutes / 60) + " hrs"
                minutes < (60 * 24 * 30) -> "hace " + (minutes / (60 * 24)) + " días"
                else -> "hace " + (minutes / (60 * 24 * 30)) + " meses"
            }
        }
    }
}