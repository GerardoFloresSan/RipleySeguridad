package com.example.myfirstapp.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.example.myfirstapp.R
import com.example.myfirstapp.data.request.CheckPricesRequest
import com.example.myfirstapp.data.response.CheckPricesResponse
import com.example.myfirstapp.presenter.CheckPricePresenter
import com.example.myfirstapp.ui.activity.checkout.PersonalInformationActivity
import com.example.myfirstapp.ui.adapter.CouponAdapter
import com.example.myfirstapp.ui.adapter.ProductAdapter
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.Methods
import com.example.myfirstapp.utils.PapersManager
import com.example.myfirstapp.utils.setColorBackground
import com.example.myfirstapp.utils.startActivityE
import kotlinx.android.synthetic.main.activity_shopping_cart.*
import java.io.Serializable
import javax.inject.Inject

class ShoppingCartActivity : RipleyBaseActivity(), CheckPricePresenter.View {

    @Inject
    lateinit var checkPricePresenter: CheckPricePresenter

    private var adapter: ProductAdapter? = null
    private var adapter2: CouponAdapter? = null

    private var mLastClickTime: Long = 0

    override fun getView(): Int = R.layout.activity_shopping_cart

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        checkPricePresenter.attachView(this)
        btn_back.setOnClickListener {
            finish()
        }

        btn_scanner_shopping_car.setOnClickListener {
            finish()
        }

        btn_delete_shopping_car.setOnClickListener {
            customDialog(getString(R.string.str_dialog_shopping_cart_delete)) {
                if (it) {
                    RipleyApplication.closeAll()
                    startActivityE(LocationStoreActivity::class.java)
                }
            }
        }

        btn_add_code_dialog_shopping_car.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            showDialog()
        }

        btn_add_cupon.setOnClickListener {
            startActivityE(CuponScanActivity::class.java)
        }

        btn_next_shopping_cart.setOnClickListener {
            startActivityE(PersonalInformationActivity::class.java)
        }
    }

    override fun onResume() {
        checkPricePresenter.attachView(this)
        setCountProductsInList(PapersManager.shoppingCart)
        config()
        disabledIfCount()
        super.onResume()
    }

    @SuppressLint("MissingPermission")
    override fun onPause() {
        checkPricePresenter.detachView()
        super.onPause()
    }

    @SuppressLint("SetTextI18n")
    private fun setCountProductsInList(shopping: CheckPricesResponse) {
        btn_next_shopping_cart.isEnabled = shopping.products.isNotEmpty()
        btn_next_shopping_cart.isClickable = shopping.products.isNotEmpty()
        btn_next_shopping_cart.isFocusable = shopping.products.isNotEmpty()

        if (shopping.products.isEmpty()) {
            btn_delete_shopping_car.visibility = View.GONE
            txt_total_other_payed.text = "S/ 0.00"
            txt_total_shopping_car.text = "S/ 0.00"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btn_next_shopping_cart.backgroundTintList =
                    ColorStateList.valueOf(getColor(R.color.colorPrimaryOpa))
            } else {
                btn_next_shopping_cart.background.setTint(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimaryOpa
                    )
                )
            }
        } else {
            btn_delete_shopping_car.visibility = View.VISIBLE
            txt_total_other_payed.text = Methods.formatMoney((shopping.totalPromo.toDouble() / 100))

                //"S/ " + "%.2f".format((shopping.totalPromo.toDouble() / 100).toBigDecimal())
            txt_total_shopping_car.text = Methods.formatMoney((shopping.totalRipley.toDouble() / 100))
                //"S/ " + "%.2f".format((shopping.totalRipley.toDouble() / 100).toBigDecimal())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btn_next_shopping_cart.backgroundTintList =
                    ColorStateList.valueOf(getColor(R.color.colorPrimary))
            } else {
                btn_next_shopping_cart.background.setTint(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                    )
                )
            }
        }
        disabledIfCount()
        setAdapter()//TODO REMOVE IF ERROR
    }

    private fun disabledIfCount() {
        val countAllProductInCar: Int = PapersManager.shoppingCart.products.size

        val validation = countAllProductInCar < maxGlobalProduct

        if (validation) {
            img_finger_cart.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
            text_cod_cart.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            val drawable = btn_add_code_dialog_shopping_car.background as GradientDrawable
            drawable.setStroke(2, ContextCompat.getColor(this, R.color.colorPrimary))
        }
        else {
            img_finger_cart.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryOpa))
            text_cod_cart.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryOpa))
            val drawable = btn_add_code_dialog_shopping_car.background as GradientDrawable
            drawable.setStroke(2, ContextCompat.getColor(this, R.color.colorPrimaryOpa))
        }

        btn_add_code_dialog_shopping_car.isEnabled = validation
        btn_add_code_dialog_shopping_car.isClickable = validation
        btn_add_code_dialog_shopping_car.isFocusable = validation

        btn_scanner_shopping_car.isEnabled = validation
        btn_scanner_shopping_car.isClickable = validation
        btn_scanner_shopping_car.isFocusable = validation

        btn_add_cupon.isEnabled = (countAllProductInCar != 0)
        btn_add_cupon.isClickable = (countAllProductInCar != 0)
        btn_add_cupon.isFocusable = (countAllProductInCar != 0)

        btn_add_cupon.setColorBackground(countAllProductInCar != 0, this, R.color.colorBlackRipley, R.color.colorGreyRipley)
        btn_scanner_shopping_car.setColorBackground(validation, this, R.color.colorPrimary, R.color.colorPrimaryOpa)
        btn_scanner_shopping_car.setColorBackground(validation, this, R.color.colorPrimary, R.color.colorPrimaryOpa)

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {btn_scanner_shopping_car.backgroundTintList = if (validation) ColorStateList.valueOf(getColor(R.color.colorPrimary))
        else ColorStateList.valueOf(getColor(R.color.colorPrimaryOpa))}else { btn_scanner_shopping_car.background.setTint(ContextCompat.getColor(this,if (validation) R.color.colorPrimary else R.color.colorPrimaryOpa))}*/

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

        val btnCancel: AppCompatButton =
            dialog.findViewById<View>(R.id.btn_cancel) as AppCompatButton
        val btnOk: AppCompatButton = dialog.findViewById<View>(R.id.btn_ok) as AppCompatButton
        val btnDelete: AppCompatImageButton =
            dialog.findViewById<View>(R.id.btn_delete) as AppCompatImageButton

        val textCode: AppCompatTextView =
            dialog.findViewById<View>(R.id.code_text) as AppCompatTextView
        val textError: AppCompatTextView =
            dialog.findViewById<View>(R.id.text_error_dialog) as AppCompatTextView

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
            addText(textCode, "0", btnOk)
        }
        btnAdd1.setOnClickListener {
            addText(textCode, "1", btnOk)
        }
        btnAdd2.setOnClickListener {
            addText(textCode, "2", btnOk)
        }
        btnAdd3.setOnClickListener {
            addText(textCode, "3", btnOk)
        }
        btnAdd4.setOnClickListener {
            addText(textCode, "4", btnOk)
        }
        btnAdd5.setOnClickListener {
            addText(textCode, "5", btnOk)
        }
        btnAdd6.setOnClickListener {
            addText(textCode, "6", btnOk)
        }
        btnAdd7.setOnClickListener {
            addText(textCode, "7", btnOk)
        }
        btnAdd8.setOnClickListener {
            addText(textCode, "8", btnOk)
        }
        btnAdd9.setOnClickListener {
            addText(textCode, "9", btnOk)
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
            dialog.dismiss()
        }

        btnOk.setOnClickListener {
            if (validLocation()) {
                checkPricePresenter.checkPriceListener(
                    textCode.text.toString().trim().padStart(13, '0'),
                    1,
                    true,
                    listener = { i, checkPricesResponse, code, skuIsMaxProduct ->
                        when (i) {
                            200 -> {
                                textError.visibility = View.INVISIBLE
                                PapersManager.shoppingCart = checkPricesResponse
                                config()
                                setCountProductsInList(checkPricesResponse)
                                dialog.dismiss()
                            }
                            305 -> {
                                textError.visibility = View.VISIBLE
                                textError.text =
                                    "No se puede agregar más de $maxGlobalProduct productos diferentes."
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

    private fun setAdapter() {
        @Suppress("SENSELESS_COMPARISON")
        if (adapter == null) {
            adapter = ProductAdapter { status, product, quantity ->
                when (status) {
                    0 -> checkPricePresenter.checkPriceComplete(
                        product.sku,
                        quantity.toLong(),
                        false
                    )//res
                    1 -> checkPricePresenter.checkPriceComplete(
                        product.sku,
                        quantity.toLong(),
                        false
                    )//sum
                    2 -> checkPricePresenter.checkPriceComplete(product.sku, 0, false)//delete
                }
            }
            recycler.removeAllViews()
            recycler.removeAllViewsInLayout()
            recycler.adapter = adapter
            adapter!!.data = PapersManager.shoppingCart.products
        } else {
            adapter!!.data = PapersManager.shoppingCart.products
            adapter!!.notifyDataSetChanged()
        }
        if (adapter2 == null) {
            adapter2 = CouponAdapter() { status, code, quantity ->
                when (status) {
                    2 -> checkPricePresenter.cuponComplete(code, false)
                }
            }
            recycler2.removeAllViews()
            recycler2.removeAllViewsInLayout()
            recycler2.adapter = adapter2
            adapter2!!.data = PapersManager.shoppingCart.coupons
        } else {
            adapter2!!.data = PapersManager.shoppingCart.coupons
            adapter2!!.notifyDataSetChanged()
        }
    }

    private fun config() {
        if (PapersManager.shoppingCart.products.isEmpty() && PapersManager.shoppingCart.coupons.isEmpty()) {
            recycler.visibility = View.GONE
            recycler2.visibility = View.GONE
            linear_empty.visibility = View.VISIBLE
            setAdapter()
        } else {
            linear_empty.visibility = View.GONE
            recycler.visibility = View.VISIBLE
            if (PapersManager.shoppingCart.coupons.isEmpty()) {
                recycler2.visibility = View.GONE
            } else {
                recycler2.visibility = View.VISIBLE
            }

            setAdapter()
        }
    }

    override fun checkSuccessPresenter(status: Int, vararg args: Serializable) {
        when (status) {
            200 -> {
                PapersManager.shoppingCart = (args[0] as CheckPricesResponse)
                config()
                setCountProductsInList((args[0] as CheckPricesResponse))
            }
            else -> {
                MaterialDialog.Builder(this)
                    .title("Advertencia")
                    .cancelable(false)
                    .content("Vuelva a intentarlo")
                    .positiveText("Ok")
                    .show()
            }
        }
    }
}