package com.example.myfirstapp.ui.activity.security

import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.appcompat.widget.AppCompatTextView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.SalesGetByResponse
import com.example.myfirstapp.ui.adapter.ProductAdapter
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.Methods
import com.example.myfirstapp.utils.startActivityE
import com.example.myfirstapp.utils.toSimpleString
import com.example.myfirstapp.utils.toSimpleTime
import kotlinx.android.synthetic.main.activity_detail_shop.*
import java.text.SimpleDateFormat

class DetailShopActivity : RipleyBaseActivity() {

    lateinit var sale: SalesGetByResponse
    private var adapter: ProductAdapter? = null

    var views: Int? = null
    var size = 0
    private var isBig = true

    override fun getView(): Int = R.layout.activity_detail_shop

    override fun onCreate() {
        sale = intent.getSerializableExtra("extra0") as SalesGetByResponse
        //TODO el nuevo campo se llama views la cantidad de cuantas veces hemos preguntado por la misma orden!

        txt_number_id.text = "Orden de compra: ${sale.orderId}"

        if (sale.trxNumber == null) {
            txt_number_order.text = "Pendiente"
        } else {
            txt_number_order.text = "N° de transacción: ${sale.trxNumber}"
        }


        txt_name_person.text = sale.clientName + " " + sale.clientLast
        txt_subsidiary.text = "Ripley " + sale.subsidiaryName

        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sale.date)
        txt_date.text = date.toSimpleString()
        txt_time.text = date.toSimpleTime()

        adapter = ProductAdapter()
        recycler.removeAllViews()
        recycler.removeAllViewsInLayout()
        recycler.adapter = adapter
        adapter!!.data = sale.products
        btn_back.setOnClickListener {
            finish()
        }

        views = sale.views
        when(views) {
            null -> {
                btn_views_order.visibility = View.INVISIBLE
            }
            else -> {
                isBig = true
                size = btn_views_order.layoutParams.width
                btn_views_order.text = views!!.toString()
                btn_views_order.setOnClickListener {
                    val changeBounds = ChangeBounds()
                    changeBounds.startDelay = 300
                    changeBounds.interpolator = AnticipateOvershootInterpolator()
                    changeBounds.duration = 500
                    TransitionManager.beginDelayedTransition(toolbar_custom, changeBounds)
                    toggleSize(btn_views_order)

                }
            }
        }

        txt_total_other_payed.text = Methods.formatMoney((sale.totalAmount.toDouble() / 100))

        when (sale.statusId) {
            2 -> {
                btn_next_shopping_cart.visibility = View.VISIBLE
                btn_check_shopping_cart.visibility = View.GONE
            }
            3 -> {
                btn_next_shopping_cart.visibility = View.GONE
                btn_check_shopping_cart.visibility = View.VISIBLE
            }
            else -> {
                btn_next_shopping_cart.visibility = View.GONE
                btn_check_shopping_cart.visibility = View.VISIBLE
            }
        }

        val clientSos = Methods.getParameter("sgRiskClientText").value

        if (sale.riskClient) {
            lnl_client_sos.visibility = View.VISIBLE
            txt_sos_client.text = clientSos
        } else {
            lnl_client_sos.visibility = View.GONE
        }


        btn_next_shopping_cart.setOnClickListener {
            startActivityE(FirmActivity::class.java, sale)
        }

        super.onCreate()
    }

    fun toggleSize(v: View) {
        val params = v.layoutParams
        if (isBig) {
            params.width = size*4 + 16
            params.height = size
            (v as AppCompatTextView).text = "${views!!.toString()} Intento de validación"
            isBig = false
        } else {
            params.width = size
            params.height = size
            (v as AppCompatTextView).text = views!!.toString()
            isBig = true
        }
        v.layoutParams = params
    }

}