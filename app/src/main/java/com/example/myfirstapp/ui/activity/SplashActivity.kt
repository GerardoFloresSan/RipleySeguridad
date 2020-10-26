package com.example.myfirstapp.ui.activity

import android.os.Bundle
import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.Parameter
import com.example.myfirstapp.presenter.ParameterPresenter
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.PapersManager
import com.example.myfirstapp.utils.startActivityE
import java.io.Serializable
import javax.inject.Inject

class SplashActivity : RipleyBaseActivity(), ParameterPresenter.View  {

    @Inject
    lateinit var parameterPresenter: ParameterPresenter

    override fun getView(): Int = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        parameterPresenter.attachView(this)
    }

    override fun onResume() {
        parameterPresenter.attachView(this)
        parameterPresenter.getParameters()
        super.onResume()
    }

    override fun onPause() {
        parameterPresenter.detachView()
        super.onPause()
    }

    override fun parameterSuccessPresenter(status: Int, vararg args: Serializable) {
        when (status) {
            200 -> {
                PapersManager.parametersAll = args[0] as ArrayList<Parameter>
                startActivityE(LocationStoreActivity::class.java)
            }
            else -> parameterPresenter.getParameters()
        }
    }
}