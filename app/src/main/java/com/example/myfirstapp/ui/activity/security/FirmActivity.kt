package com.example.myfirstapp.ui.activity.security

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Build
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.myfirstapp.R
import com.example.myfirstapp.data.request.CloseCartRequest
import com.example.myfirstapp.data.response.CloseCartResponse
import com.example.myfirstapp.data.response.SalesGetByResponse
import com.example.myfirstapp.presenter.SalesPresenter
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.Methods
import com.example.myfirstapp.utils.PapersManager
import com.example.myfirstapp.utils.startActivityE
import kotlinx.android.synthetic.main.activity_firma.*
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.Serializable
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class FirmActivity : RipleyBaseActivity(), CoroutineScope, SalesPresenter.View {

    @Inject
    lateinit var salesPresenter: SalesPresenter

    lateinit var sale: SalesGetByResponse

    private lateinit var job: Job

    override fun getView(): Int = R.layout.activity_firma

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        salesPresenter.attachView(this)

        sale = intent.getSerializableExtra("extra0") as SalesGetByResponse

        job = Job()

        sssvCheckoutSign.setOnClickListener {
            set(!sssvCheckoutSign.isCanvasBlank())
        }

        btn_clean.setOnClickListener {
            sssvCheckoutSign.clearCanvas()
            set(!sssvCheckoutSign.isCanvasBlank())
        }
        btn_valid.setOnClickListener {
            if (!sssvCheckoutSign.isCanvasBlank()) {
                main()
            } else {
                toast("Ingrese su firma")
            }
        }
        btn_skip.setOnClickListener {
            close("")
        }
        set(!sssvCheckoutSign.isCanvasBlank())

        if (Methods.getParameter("sgSignatureInd").value == "1") {
            btn_skip.visibility = View.GONE
        } else {
            btn_skip.visibility = View.VISIBLE
        }
    }

    fun main() = runBlocking {
        launch(Unconfined) {
            getSign()
        }
    }

    private fun set(validation: Boolean) {
        @SuppressLint("SetTextI18n")
        btn_valid.isEnabled = validation
        btn_valid.isClickable = validation
        btn_valid.isFocusable = validation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            btn_valid.backgroundTintList = ColorStateList.valueOf(
                if (validation) getColor(R.color.colorPrimary) else getColor(R.color.colorPrimaryOpa)
            )
        } else {
            btn_valid.background.setTint(
                ContextCompat.getColor(
                    this,
                    if (validation) R.color.colorPrimary else R.color.colorPrimaryOpa
                )
            )
        }
    }

    override fun onResume() {
        salesPresenter.attachView(this)
        super.onResume()
    }

    override fun onPause() {
        salesPresenter.detachView()
        super.onPause()
    }

    @Suppress("SENSELESS_COMPARISON")
    fun close(sign: String) {
        salesPresenter.closeSales(CloseCartRequest().apply {
            this.orderId = sale.orderId.toString()
            this.hashQr = if (sale.hashQr != null) sale.hashQr else ""
            this.clientSignature = sign
            this.username = PapersManager.username
            this.token = PapersManager.loginAccess.token
            this.latitude = PapersManager.locationUser.latitude
            this.longitude = PapersManager.locationUser.longitude
        })


    }

    suspend fun getSign() {
        val sign = sssvCheckoutSign.getSign()
        val encoded = convert(sign)
        if (encoded != null) {
            runOnUiThread {
                close(encoded.replace("\n", ""))
            }
        }
    }

    fun convert(bitmap: Bitmap): String? {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return Base64.encodeToString(
            outputStream.toByteArray(),
            Base64.DEFAULT
        )
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override fun salesSuccessPresenter(status: Int, vararg args: Serializable) {
        when (status) {
            200 -> {
                if (PapersManager.device.contains(Methods.getNameModelDevice()!!.toLowerCase())) {
                    startActivityE(EndOrderActivity::class.java, args[0] as CloseCartResponse)
                } else {
                    startActivityE(EndOrderAppActivity::class.java, args[0] as CloseCartResponse)
                }
            }
            else -> {
                showError(args[0] as String)
            }
        }
    }
}