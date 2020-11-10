package com.example.myfirstapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
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


    /*private fun primeraLetraMayuscula(cadena: String?): String {
        if (cadena == null || cadena.isEmpty()) {
            return ""
        }
        val primeraLetra = cadena[0]
        return if (Character.isUpperCase(primeraLetra)) {
            cadena
        } else {
            Character.toUpperCase(primeraLetra).toString() + cadena.substring(1)
        }
    }*/

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
        private val CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        private val RANDOM_STRING_LENGTH = 15
        @SuppressLint("StaticFieldLeak")
        private var methods: Methods? = null

        fun init(context: Context) {
            methods = Methods(context)
        }

        private fun getRandomNumber(): Int {
            var randomInt = 0
            val randomGenerator = Random()
            randomInt = randomGenerator.nextInt(CHAR_LIST.length)
            return if (randomInt - 1 == -1) {
                randomInt
            } else {
                randomInt - 1
            }
        }

        fun generateRandomString(): String {
            val randStr = StringBuffer()
            for (i in 0 until RANDOM_STRING_LENGTH) {
                val number = getRandomNumber()
                val ch = CHAR_LIST[number]
                randStr.append(ch)
            }
            return randStr.toString()
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
            return NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(number).replace("/", "/ ").replace("/ .", "/ ")
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

        fun getNameModelDevice(): String? {
            return Build.MODEL
        }

        fun generateQRCode(text: String): Bitmap {
            val width = 220
            val height = 220
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val codeWriter = MultiFormatWriter()
            try {
                val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
            } catch (e: WriterException) {
                Log.d("TAG", "generateQRCode: ${e.message}")
            }
            return bitmap
        }
    }
}