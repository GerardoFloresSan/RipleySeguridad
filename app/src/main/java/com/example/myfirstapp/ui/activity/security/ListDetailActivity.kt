package com.example.myfirstapp.ui.activity.security

import com.example.myfirstapp.R
import com.example.myfirstapp.data.request.GetStateByQrRequest
import com.example.myfirstapp.data.response.SalesGetByResponse
import com.example.myfirstapp.presenter.SalesPresenter
import com.example.myfirstapp.ui.adapter.SalesAdapter
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.PapersManager
import com.example.myfirstapp.utils.startActivityE
import kotlinx.android.synthetic.main.activity_list_detail.*
import java.io.Serializable
import javax.inject.Inject

class ListDetailActivity : RipleyBaseActivity(), SalesPresenter.View {

    @Inject
    lateinit var salesPresenter: SalesPresenter
    lateinit var listSales : ArrayList<SalesGetByResponse>
    private var adapter: SalesAdapter? = null

    override fun getView(): Int = R.layout.activity_list_detail

    @Suppress("UNCHECKED_CAST")
    override fun onCreate() {
        component.inject(this)
        salesPresenter.attachView(this)
        listSales = intent.getSerializableExtra("extra0") as ArrayList<SalesGetByResponse>
        btn_back.setOnClickListener {
            finish()
        }
        adapter = SalesAdapter { status, sale ->
            when (status) {
                0 -> {
                    if(sale.hashQr.isNullOrEmpty()) {
                        startActivityE(DetailShopActivity::class.java, sale)
                    } else {
                        salesPresenter.getUserByQr(GetStateByQrRequest().apply {
                            this.hashQr = sale.hashQr
                            this.username = PapersManager.username
                            this.token = PapersManager.loginAccess.token
                        })
                    }
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

    override fun salesSuccessPresenter(status: Int, vararg args: Serializable) {
        val response = args[0] as SalesGetByResponse
        startActivityE(DetailShopActivity::class.java, response)
    }
}