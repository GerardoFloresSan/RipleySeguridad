package com.example.myfirstapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Environment
import com.afollestad.materialdialogs.MaterialDialog
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class ProcessBitmap(private val doStuff: DoStuff) : AsyncTask<Any, Void, String>() {

    private lateinit var dialog: MaterialDialog

    override fun onPreExecute() {
        dialog = MaterialDialog.Builder(doStuff.getContext())
                .title("Procesando...")
                .content("Espere un momento")
                .progress(true, 0)
                .cancelable(false)
                .show()
    }

    override fun doInBackground(vararg objects: Any): String? {
        var bitmap: Bitmap = objects[0] as Bitmap

        val width = 1000
        val factor = width * 1.0 / bitmap.width
        val height = (bitmap.height * factor).toInt()

        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)

        return saveImageFile(bitmap)
    }

    override fun onPostExecute(s: String?) {
        dialog.dismiss()

        if (s == null) {
            MaterialDialog.Builder(doStuff.getContext())
                    .title("Ups!")
                    .content("Hubo un error, inténtelo nuevamente")
                    .positiveText("Ok")
                    .show()
            return
        }

        doStuff.done(s)
    }

    private fun saveImageFile(bitmap: Bitmap?): String? {
        val quality = 99
        val format = Bitmap.CompressFormat.PNG

        //TODO NEED INVESTIGATE MORE
        /*val resolver = doStuff.getContext().contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "Ripley")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/${Date().time}.png")
        }

        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)*/


        /*val pathNew = StringBuilder()
        pathNew.append(Environment.getExternalStorageDirectory())
        pathNew.append(Date().time)
        pathNew.append(".png")*/

        /*val builder = StringBuilder()

        builder.append(doStuff.getContext().dataDir.absolutePath)
        builder.append("/Ripley/")
        builder.append(Date().time)
        builder.append(".png")*/
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH]
        val day = cal[Calendar.DAY_OF_MONTH]

        @Suppress("NAME_SHADOWING") val id = "${year}_${month}_${day}_${Date().time}.png"

        val file = File(Environment.getExternalStorageDirectory(), "Ripley/$id")//${Date().time}.png
        val path = File(file.parent)

        if (file.parent != null && !path.isDirectory) {
            path.mkdirs()
        }

        var outputStream: FileOutputStream? = null

        try {
            outputStream = FileOutputStream(file)
            bitmap!!.compress(format, quality, outputStream)
            return file.absolutePath.toString()
        } catch (e: FileNotFoundException) {
            return null
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush()
                    outputStream.close()
                }
            } catch (e: IOException) {
                return null
            }

            bitmap!!.recycle()
        }
    }

    interface DoStuff {
        fun getContext(): Context
        fun done(s: String)
    }
}