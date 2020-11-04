package com.example.myfirstapp.ui.activity.security

import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.SalesGetByResponse
import com.example.myfirstapp.ui.adapter.SalesAdapter
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.startActivityE
import kotlinx.android.synthetic.main.activity_list_detail.*

class ListDetailActivity : RipleyBaseActivity() {

    lateinit var listSales : ArrayList<SalesGetByResponse>
    private var adapter: SalesAdapter? = null

    override fun getView(): Int = R.layout.activity_list_detail

    @Suppress("UNCHECKED_CAST")
    override fun onCreate() {
        listSales = intent.getSerializableExtra("extra0") as ArrayList<SalesGetByResponse>
        btn_back.setOnClickListener {
            finish()
        }
        adapter = SalesAdapter { status, sale ->
            when (status) {
                0 -> {
                    startActivityE(DetailShopActivity::class.java, sale)
                }
                else -> {}
            }
        }
        recycler.removeAllViews()
        recycler.removeAllViewsInLayout()
        recycler.adapter = adapter
        adapter!!.data = listSales
        super.onCreate()
    }
}