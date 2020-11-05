package com.example.myfirstapp.ui.activity.security

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import com.example.myfirstapp.R
import com.example.myfirstapp.data.request.GetStateByDocRequest
import com.example.myfirstapp.data.request.GetStateByQrRequest
import com.example.myfirstapp.data.response.SalesGetByResponse
import com.example.myfirstapp.presenter.SalesPresenter
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.PapersManager
import com.example.myfirstapp.utils.startActivityE
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_validation.*
import java.io.Serializable
import javax.inject.Inject

class ValidationActivity : RipleyBaseActivity(), SalesPresenter.View {

    @Inject
    lateinit var salesPresenter: SalesPresenter

    private var stopScan = false

    private lateinit var cameraSource: CameraSource

    private lateinit var detector: BarcodeDetector

    private var hashQrLocal: String = ""

    override fun getView(): Int = R.layout.activity_validation

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        salesPresenter.attachView(this)

        if (checkPermissionsCamera()) {
            setupControls()
        } //
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getCameraPermission()
            }
        }

        btn_back.setOnClickListener {
            finish()
        }
        add_doc.setOnClickListener {
            showDialog()
        }
    }

    private fun setupControls() {
        detector = BarcodeDetector.Builder(this@ValidationActivity).setBarcodeFormats(Barcode.ALL_FORMATS).build()
        cameraSource = CameraSource.Builder(this@ValidationActivity, detector).setRequestedPreviewSize(1920, 1080).setAutoFocusEnabled(true).build()
        cameraSurfaceView.holder.addCallback(surfaceCallBack)
        detector.setProcessor(processor)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupControls()
                } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            toast("Camera ------ Never ask again PERMISSION_DENIED")
                        }
                    }
                }
            }
        }
    }

    private val surfaceCallBack = object : SurfaceHolder.Callback {
        override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

        }

        override fun surfaceDestroyed(p0: SurfaceHolder) {
            cameraSource.stop()
        }

        @SuppressLint("MissingPermission")
        override fun surfaceCreated(p0: SurfaceHolder) {
            try {
                cameraSource.start(p0)
            } catch (e: Exception) {
            }
        }
    }

    private val processor = object : Detector.Processor<Barcode> {
        override fun release() {
        }

        override fun receiveDetections(p0: Detector.Detections<Barcode>?) {
            runOnUiThread {
                if (!stopScan) {
                    if (p0 != null && p0.detectedItems.isNotEmpty()) {
                        val qrCodes: SparseArray<Barcode> = p0.detectedItems
                        val code = qrCodes.valueAt(0).displayValue
                        stopScan = true
                        hashQrLocal = code
                        salesPresenter.getUserByQr(GetStateByQrRequest().apply {
                            this.hashQr = code
                            this.username = PapersManager.username
                            this.token = PapersManager.loginAccess.token
                        })
                    }
                }
            }
        }
    }

    override fun onResume() {
        salesPresenter.attachView(this)
        stopScan = false
        super.onResume()
    }

    override fun onPause() {
        stopScan = true
        hideLoading()
        salesPresenter.detachView()
        super.onPause()
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_keyboard_numeric)

        val btnAdd0: AppCompatButton = dialog.findViewById<View>(R.id.btn_0) as AppCompatButton
        val btnAdd1: AppCompatButton = dialog.findViewById<View>(R.id.btn_1) as AppCompatButton
        val btnAdd2: AppCompatButton = dialog.findViewById<View>(R.id.btn_2) as AppCompatButton
        val btnAdd3: AppCompatButton = dialog.findViewById<View>(R.id.btn_3) as AppCompatButton
        val btnAdd4: AppCompatButton = dialog.findViewById<View>(R.id.btn_4) as AppCompatButton
        val btnAdd5: AppCompatButton = dialog.findViewById<View>(R.id.btn_5) as AppCompatButton
        val btnAdd6: AppCompatButton = dialog.findViewById<View>(R.id.btn_6) as AppCompatButton
        val btnAdd7: AppCompatButton = dialog.findViewById<View>(R.id.btn_7) as AppCompatButton
        val btnAdd8: AppCompatButton = dialog.findViewById<View>(R.id.btn_8) as AppCompatButton
        val btnAdd9: AppCompatButton = dialog.findViewById<View>(R.id.btn_9) as AppCompatButton

        val btnCancel: AppCompatButton = dialog.findViewById<View>(R.id.btn_cancel) as AppCompatButton
        val btnOk: AppCompatButton = dialog.findViewById<View>(R.id.btn_ok) as AppCompatButton
        val btnDelete: AppCompatImageButton = dialog.findViewById<View>(R.id.btn_delete) as AppCompatImageButton

        val textCode: AppCompatTextView = dialog.findViewById<View>(R.id.code_text) as AppCompatTextView
        val textError: AppCompatTextView = dialog.findViewById<View>(R.id.text_error_dialog) as AppCompatTextView

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.attributes?.windowAnimations = R.style.AppTheme_Slide
        dialog.setCancelable(false)

        dialog.show()
        btnOk.isEnabled = false
        btnOk.isClickable = false
        btnOk.isFocusable = false

        btnAdd0.setOnClickListener {
            addText(textCode, VALUE_ZERO, btnOk)
        }
        btnAdd1.setOnClickListener {
            addText(textCode, VALUE_ONE, btnOk)
        }
        btnAdd2.setOnClickListener {
            addText(textCode, VALUE_TWO, btnOk)
        }
        btnAdd3.setOnClickListener {
            addText(textCode, VALUE_THREE, btnOk)
        }
        btnAdd4.setOnClickListener {
            addText(textCode, VALUE_FOUR, btnOk)
        }
        btnAdd5.setOnClickListener {
            addText(textCode, VALUE_FIVE, btnOk)
        }
        btnAdd6.setOnClickListener {
            addText(textCode, VALUE_SIX, btnOk)
        }
        btnAdd7.setOnClickListener {
            addText(textCode, VALUE_SEVEN, btnOk)
        }
        btnAdd8.setOnClickListener {
            addText(textCode, VALUE_EIGHT, btnOk)
        }
        btnAdd9.setOnClickListener {
            addText(textCode, VALUE_NINE, btnOk)
        }
        btnDelete.setOnClickListener {
            textError.visibility = View.INVISIBLE
            if (textCode.length() > 0) textCode.text = textCode.text.substring(0, textCode.length() - 1)
            val validation = textCode.text.length > 7
            btnOk.isEnabled = validation
            btnOk.isClickable = validation
            btnOk.isFocusable = validation

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btnOk.backgroundTintList = ColorStateList.valueOf(if(validation) getColor(R.color.colorPrimary) else getColor(R.color.colorPrimaryOpa))
            }//
            else {
                btnOk.background.setTint(ContextCompat.getColor(this, if(validation) R.color.colorPrimary else R.color.colorPrimaryOpa))
            }
        }
        btnCancel.setOnClickListener {
            stopScan = false
            dialog.dismiss()
        }
        btnOk.setOnClickListener {
            stopScan = true
            salesPresenter.getUserByDoc(
                GetStateByDocRequest().apply {
                    this.clientDoc = textCode.text.toString().trim()
                    this.username = PapersManager.username
                    this.token = PapersManager.loginAccess.token
                }, listener = { i, response ->
                    when (i) {
                        200 -> {
                            textError.visibility = View.INVISIBLE
                            startActivityE(ListDetailActivity::class.java, response)
                            dialog.dismiss()
                        }
                        500 -> {
                            textError.visibility = View.VISIBLE
                            textError.text = "No se encuentran compras relacionadas"
                        }
                        else -> {
                            textError.visibility = View.VISIBLE
                            textError.text = "No se encuentran compras relacionadas"
                        }
                    }
                })
        }
    }

    private fun addText(textCode: AppCompatTextView, number: String, btnOk: AppCompatButton) {
        @SuppressLint("SetTextI18n")

        val validation = textCode.text.length >= 7

        textCode.text = "${textCode.text}$number"

        btnOk.isEnabled = validation
        btnOk.isClickable = validation
        btnOk.isFocusable = validation


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            btnOk.backgroundTintList = ColorStateList.valueOf(if(validation) getColor(R.color.colorPrimary) else getColor(R.color.colorPrimaryOpa))
        }//
        else {
            btnOk.background.setTint(ContextCompat.getColor(this, if(validation) R.color.colorPrimary else R.color.colorPrimaryOpa))
        }
    }

    override fun salesSuccessPresenter(status: Int, vararg args: Serializable) {
        stopScan = false
        when(status) {
            200 -> {
                val list : ArrayList<SalesGetByResponse> = arrayListOf()
                val t = (args[0] as SalesGetByResponse).apply {
                    this.hashQr = hashQrLocal
                }
                list.add(t)
                startActivityE(ListDetailActivity::class.java, list)
            }
            403 -> {
                toast("Orden de una sucursal diferente a la que está asignado")
            }
            else -> toast("No se encuentran compras relacionadas")
        }
    }

    companion object {
        private const val VALUE_ZERO = "0"
        private const val VALUE_ONE = "1"
        private const val VALUE_TWO = "2"
        private const val VALUE_THREE = "3"
        private const val VALUE_FOUR = "4"
        private const val VALUE_FIVE = "5"
        private const val VALUE_SIX = "6"
        private const val VALUE_SEVEN = "7"
        private const val VALUE_EIGHT = "8"
        private const val VALUE_NINE = "9"
    }
}