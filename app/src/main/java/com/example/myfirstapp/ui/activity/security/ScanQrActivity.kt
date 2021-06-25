package com.example.myfirstapp.ui.activity.security

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Build
import android.text.Editable
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.myfirstapp.R
import com.example.myfirstapp.data.request.GetStateByDocRequest
import com.example.myfirstapp.data.request.GetStateByQrRequest
import com.example.myfirstapp.data.response.SalesGetByResponse
import com.example.myfirstapp.presenter.SalesPresenter
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.*
import kotlinx.android.synthetic.main.activity_scan_qr.*
import java.io.Serializable
import javax.inject.Inject

class ScanQrActivity : RipleyBaseActivity(), SalesPresenter.View {

    @Inject
    lateinit var salesPresenter: SalesPresenter

    private var stopScan = false

    private var hashQrLocal: String = ""

    override fun getView(): Int = R.layout.activity_scan_qr
    
    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        salesPresenter.attachView(this)

        txt_scan_qr.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                if(!stopScan && txt_scan_qr.getString().isNotEmpty()) {
                    hashQrLocal = txt_scan_qr.getString()
                    salesPresenter.getUserByQr(GetStateByQrRequest().apply {
                        this.hashQr = txt_scan_qr.getString()
                        this.username = PapersManager.username
                        this.token = PapersManager.loginAccess.token
                    })
                }
            }
        })

        add_doc.setOnClickListener {
            showDialog()
        }

        btn_back.setOnClickListener {
            finish()
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
            addText(textCode, ScanQrActivity.VALUE_ZERO, btnOk)
        }
        btnAdd1.setOnClickListener {
            addText(textCode, ScanQrActivity.VALUE_ONE, btnOk)
        }
        btnAdd2.setOnClickListener {
            addText(textCode, ScanQrActivity.VALUE_TWO, btnOk)
        }
        btnAdd3.setOnClickListener {
            addText(textCode, ScanQrActivity.VALUE_THREE, btnOk)
        }
        btnAdd4.setOnClickListener {
            addText(textCode, ScanQrActivity.VALUE_FOUR, btnOk)
        }
        btnAdd5.setOnClickListener {
            addText(textCode, ScanQrActivity.VALUE_FIVE, btnOk)
        }
        btnAdd6.setOnClickListener {
            addText(textCode, ScanQrActivity.VALUE_SIX, btnOk)
        }
        btnAdd7.setOnClickListener {
            addText(textCode, ScanQrActivity.VALUE_SEVEN, btnOk)
        }
        btnAdd8.setOnClickListener {
            addText(textCode, ScanQrActivity.VALUE_EIGHT, btnOk)
        }
        btnAdd9.setOnClickListener {
            addText(textCode, ScanQrActivity.VALUE_NINE, btnOk)
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
                            startActivityE(ListDetailActivity::class.java, response, 1)
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
                txt_scan_qr.clean()
                txt_scan_qr.setText("")
                list.add(t)
                startActivityE(ListDetailActivity::class.java, list, 0)
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