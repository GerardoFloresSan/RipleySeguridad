package com.example.myfirstapp.ui.activity.security

import android.view.View
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

    override fun getView(): Int = R.layout.activity_detail_shop

    override fun onCreate() {
        sale = intent.getSerializableExtra("extra0") as SalesGetByResponse

        txt_name_person.text = sale.clientName + " " + sale.clientLast
        txt_subsidiary.text = "Ripley " + sale.subsidiaryName

        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sale.date)
        txt_date.text = date.toSimpleString().replaceFirst(" ", " de ")
        txt_time.text = date.toSimpleTime()

        adapter = ProductAdapter()
        recycler.removeAllViews()
        recycler.removeAllViewsInLayout()
        recycler.adapter = adapter
        adapter!!.data = sale.products
        btn_back.setOnClickListener {
            finish()
        }

        txt_total_other_payed.text = Methods.formatMoney((sale.totalAmount.toDouble() / 100))

        when (sale.statusId) {
            1 -> {
                btn_next_shopping_cart.visibility = View.VISIBLE
                btn_check_shopping_cart.visibility = View.GONE
            }
            2 -> {
                btn_next_shopping_cart.visibility = View.VISIBLE
                btn_check_shopping_cart.visibility = View.GONE
            }
            3 -> {
                btn_next_shopping_cart.visibility = View.VISIBLE
                btn_check_shopping_cart.visibility = View.GONE
            }
            else -> {
                btn_next_shopping_cart.visibility = View.GONE
                btn_check_shopping_cart.visibility = View.VISIBLE
            }
        }

        btn_next_shopping_cart.setOnClickListener {
            startActivityE(FirmActivity::class.java, sale)
        }

        super.onCreate()
    }

}