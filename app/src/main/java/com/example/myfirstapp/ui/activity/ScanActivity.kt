package com.example.myfirstapp.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Handler
import android.os.SystemClock
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
import com.example.myfirstapp.data.response.CheckPricesResponse
import com.example.myfirstapp.presenter.CheckPricePresenter
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.Methods
import com.example.myfirstapp.utils.PapersManager
import com.example.myfirstapp.utils.startActivityE
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_scan.*
import java.io.Serializable
import java.lang.Exception
import javax.inject.Inject

class ScanActivity : RipleyBaseActivity(), CheckPricePresenter.View {

    @Inject
    lateinit var checkPricePresenter: CheckPricePresenter

    private var stopScan = false

    private lateinit var cameraSource: CameraSource

    private lateinit var detector: BarcodeDetector

    private var mLastClickTime: Long = 0

    override fun getView(): Int = R.layout.activity_scan

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        checkPricePresenter.attachView(this)

        if (checkPermissionsCamera()) {
            setupControls()
        } //
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getCameraPermission()
            }
        }

        btn_back.setOnClickListener {
            back()
        }

        add_code_dialog.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            stopScan = true
            showDialog()
        }

        btn_shopping_car_list.setOnClickListener {
            if (validLocation()) {
                startActivityE(ShoppingCartActivity::class.java)
            }
        }

    }

    override fun onBackPressed() {
        back()
    }

    private fun back() {
        if (PapersManager.shoppingCart.products.isEmpty()) {
            startActivityE(LocationStoreActivity::class.java)
        }//
        else {
            stopScan = true
            validCancelBack()
        }
    }

    private fun validCancelBack() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.alert_delete_cart_shop)

        val btnCancel: AppCompatButton =
            dialog.findViewById<View>(R.id.btn_cancel) as AppCompatButton
        val btnOk: AppCompatButton = dialog.findViewById<View>(R.id.btn_ok) as AppCompatButton

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.attributes?.windowAnimations = R.style.AppTheme_Slide
        dialog.setCancelable(false)
        dialog.show()

        btnCancel.setOnClickListener {
            dialog.dismiss()
            stopScan = false
        }
        btnOk.setOnClickListener {
            dialog.dismiss()
            stopScan = false
            startActivityE(LocationStoreActivity::class.java)
            PapersManager.shoppingCart = CheckPricesResponse()
        }
    }

    private fun setupControls() {
        detector = BarcodeDetector.Builder(this@ScanActivity).setBarcodeFormats(Barcode.ALL_FORMATS).build()
        cameraSource = CameraSource.Builder(this@ScanActivity, detector).setRequestedPreviewSize(1920, 1080).setAutoFocusEnabled(true).build()
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
                        scanBarCode(code)
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        checkPricePresenter.attachView(this)
        setCountProductsInList(PapersManager.shoppingCart)
        super.onResume()
    }

    @SuppressLint("MissingPermission")
    override fun onPause() {
        checkPricePresenter.detachView()
        super.onPause()
    }

    @SuppressLint("SetTextI18n")
    private fun setCountProductsInList(shopping: CheckPricesResponse) {
        var count: Long = 0
        for (p in shopping.products) {
            count += p.quantity
        }

        if (count == 0.toLong()) {
            txt_number_product.visibility = View.VISIBLE
            txt_number_product.text = "0"
            txt_total_money_car.text = "S/ 0.00"
        }//
        else {
            txt_number_product.visibility = View.VISIBLE
            txt_number_product.text = count.toString()
            txt_total_money_car.text = Methods.formatMoney((shopping.totalPromo.toDouble() / 100))//"S/ " + "%.2f".format((shopping.totalPromo.toDouble()/100).toBigDecimal())
        }
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

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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
            if (textCode.length() > 0) textCode.text =
                textCode.text.substring(0, textCode.length() - 1)
            btnOk.isEnabled = textCode.text.isNotEmpty()
            btnOk.isClickable = textCode.text.isNotEmpty()
            btnOk.isFocusable = textCode.text.isNotEmpty()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btnOk.backgroundTintList =
                    if (textCode.text.isNotEmpty()) ColorStateList.valueOf(getColor(R.color.colorPrimary)) else ColorStateList.valueOf(
                        getColor(R.color.colorPrimaryOpa)
                    )
            }//
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (textCode.text.isNotEmpty())
                        btnOk.background.setTint(ContextCompat.getColor(this, R.color.colorPrimary))
                    else
                        btnOk.background.setTint(
                            ContextCompat.getColor(
                                this,
                                R.color.colorPrimaryOpa
                            )
                        )
                }
            }
        }
        btnCancel.setOnClickListener {
            stopScan = false
            dialog.dismiss()
        }
        btnOk.setOnClickListener {
            if (validLocation()) {
                stopScan = true
                checkPricePresenter.checkPriceListener(textCode.text.toString().trim().padStart(13, '0'), 1, true, listener = { i, checkPricesResponse, code, skuIsMaxProduct ->
                    when (i) {
                        200 -> {
                            textError.visibility = View.INVISIBLE
                            setDataProductCard(checkPricesResponse, code, skuIsMaxProduct)
                            dialog.dismiss()
                        }
                        305 -> {
                            dialog.dismiss()
                            errorMessage("No se puede agregar más de $maxGlobalProduct productos diferentes.")
                            /* TODO IF CHANGED IDEA
                            textError.visibility = View.VISIBLE
                            textError.text = "No se puede agregar más de $maxGlobalProduct productos diferentes."
                            */
                        }
                        else -> {
                            textError.visibility = View.VISIBLE
                            textError.text = "El código ingresado es inválido."
                        }
                    }
                })
            }
        }
    }

    fun scanBarCode(code: String) {
        checkPricePresenter.checkPriceComplete(code, 1, true)
    }

    private fun addText(textCode: AppCompatTextView, number: String, btnOk: AppCompatButton) {
        @SuppressLint("SetTextI18n")
        textCode.text = "${textCode.text}$number"

        btnOk.isEnabled = textCode.text.isNotEmpty()
        btnOk.isClickable = textCode.text.isNotEmpty()
        btnOk.isFocusable = textCode.text.isNotEmpty()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            btnOk.backgroundTintList =
                if (textCode.text.isNotEmpty()) ColorStateList.valueOf(getColor(R.color.colorPrimary)) else ColorStateList.valueOf(
                    getColor(R.color.colorPrimaryOpa)
                )
        }//
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (textCode.text.isNotEmpty())
                    btnOk.background.setTint(ContextCompat.getColor(this, R.color.colorPrimary))
                else
                    btnOk.background.setTint(ContextCompat.getColor(this, R.color.colorPrimaryOpa))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDataProductCard(check: CheckPricesResponse, code: String, skuIsMaxProduct: Boolean) {
        var product : CheckPricesResponse.Product = CheckPricesResponse.Product()

        for (p in check.products) {
            if(p.sku == code) {
                product = p
                break
            }
        }

        txt_error_scan.visibility = View.INVISIBLE
        card_add_product.visibility = View.VISIBLE
        tans_ocp.visibility = View.VISIBLE //Todo nuevo
        add_code_dialog.visibility = View.GONE

        btn_back.isEnabled = false
        btn_back.isClickable = false
        btn_back.isFocusable = false

        btn_shopping_car_list.isEnabled = false
        btn_shopping_car_list.isClickable = false
        btn_shopping_car_list.isFocusable = false

        txt_title_product.text = product.description
        txt_description_product.text = "SKU: ${product.sku}"

        txt_price_normal.text = Methods.formatMoney((product.pricePromo.toDouble() / 100))// "S/ " + "%.2f".format(((product.pricePromo.toDouble() / 100)).toBigDecimal())
        txt_price_ripley.text = Methods.formatMoney((product.priceRipley.toDouble() / 100))//"S/ " + "%.2f".format(((product.priceRipley.toDouble() / 100)).toBigDecimal())

        txt_count_product.text = product.quantity.toString()
        val count = product.quantity
        btn_one_rest.setOnClickListener {
            if (count > 1) {
                val rest = (count - 1)
                checkPricePresenter.checkPriceComplete(code, rest, false)
            }
        }
        btn_one_more.setOnClickListener {
            if (count < maxByProduct) {
                val sum = (count + 1)
                checkPricePresenter.checkPriceComplete(code, sum, false)
            }
        }
        btn_close_add.setOnClickListener {
            stopScan = false
            card_add_product.visibility = View.GONE
            tans_ocp.visibility = View.GONE //Todo nuevo
            add_code_dialog.visibility = View.VISIBLE

            btn_back.isEnabled = true
            btn_back.isClickable = true
            btn_back.isFocusable = true

            btn_shopping_car_list.isEnabled = true
            btn_shopping_car_list.isClickable = true
            btn_shopping_car_list.isFocusable = true
        }
        btn_ok.setOnClickListener {
            if (validLocation()) {
                stopScan = false
                card_add_product.visibility = View.GONE
                tans_ocp.visibility = View.GONE //Todo nuevo

                btn_back.isEnabled = true
                btn_back.isClickable = true
                btn_back.isFocusable = true

                btn_shopping_car_list.isEnabled = true
                btn_shopping_car_list.isClickable = true
                btn_shopping_car_list.isFocusable = true

                add_code_dialog.visibility = View.VISIBLE

                //setCountProductsInList(shopping)
                var inList = false
                if(check.products.size > maxGlobalProduct) {
                    for (p in check.products) {
                        if (p.sku == code) {
                            inList = true
                        }
                    }
                }

                if(inList) {
                    errorMessage("No se puede agregar más de $maxGlobalProduct productos diferentes.")
                }
                else {
                    if(skuIsMaxProduct && product.quantity.toInt() == maxByProduct) {
                        errorMessage("No se puede agregar más de $maxByProduct veces el mismo producto")
                    }
                    else {
                        toast_add_product.text = "Producto agregado al carrito ✓"
                        toast_add_product.animate().scaleX(1f).scaleY(1f).setDuration(1500).start()
                        toast_add_product.text = "Producto agregado al carrito ✓"
                        PapersManager.shoppingCart = check
                        setCountProductsInList(check)
                        Handler().postDelayed({toast_add_product.animate().scaleX(0f).scaleY(0f).setDuration(500).start()}, 3000)
                    }
                }
            }
        }
    }//"No se puede agregar más de $maxGlobalProduct productos diferentes."

    override fun checkSuccessPresenter(status: Int, vararg args: Serializable) {
        when (status) {
            200 -> {
                setDataProductCard((args[0] as CheckPricesResponse), (args[1] as String), (args[2] as Boolean))
            }
            305 -> {
                errorMessage("No se puede agregar más de $maxGlobalProduct productos diferentes.")
            }
            else -> {
                errorMessage("No se puede reconocer el código")
            }
        }
    }

    private fun errorMessage(error: String) {
        txt_error_scan.visibility = View.VISIBLE
        txt_error_scan.text = error
        Handler().postDelayed({
            txt_error_scan.visibility = View.INVISIBLE
            stopScan = false
        }, 3000)
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