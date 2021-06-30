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
    lateinit var listSales: ArrayList<SalesGetByResponse>
    private var adapter: SalesAdapter? = null
    private var type: Int = 0

    private var hashQrLocal: String = ""

    override fun getView(): Int = R.layout.activity_list_detail

    @Suppress("UNCHECKED_CAST")
    override fun onCreate() {
        component.inject(this)
        salesPresenter.attachView(this)
        listSales = intent.getSerializableExtra("extra0") as ArrayList<SalesGetByResponse>
        type = intent.getIntExtra("extra1", 0)
        btn_back.setOnClickListener {
            finish()
        }
        adapter = SalesAdapter { status, sale ->
            when (status) {
                0 -> {
                    if (sale.hashQr.isNullOrEmpty() || type == 0) {
                        startActivityE(DetailShopActivity::class.java, sale)
                    } else {
                        hashQrLocal = sale.hashQr
                        salesPresenter.getUserByQr2(GetStateByQrRequest().apply {
                            this.hashQr = sale.hashQr
                            this.username = PapersManager.username
                            this.token = PapersManager.loginAccess.token
                        })
                    }
                }
                else -> {
                }
            }
        }
        recycler.removeAllViews()
        recycler.removeAllViewsInLayout()
        recycler.adapter = adapter
        adapter!!.data = listSales
        super.onCreate()
    }

    override fun salesSuccessPresenter(status: Int, vararg args: Serializable) {
        when (status) {
            300 -> {
                val response = args[0] as SalesGetByResponse
                response.apply {
                    this.hashQr = hashQrLocal
                }
                startActivityE(DetailShopActivity::class.java, response)
            }
            403 -> {
                toast("Orden de una sucursal diferente a la que está asignado")
            }
            else -> toast("No se encuentra compra relacionada")
        }
    }
}