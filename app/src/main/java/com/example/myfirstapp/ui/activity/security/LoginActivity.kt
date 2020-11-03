package com.example.myfirstapp.ui.activity.security

import android.os.Bundle
import android.text.Editable
import android.view.View
import com.example.myfirstapp.BuildConfig
import com.example.myfirstapp.R
import com.example.myfirstapp.data.request.LoginRequest
import com.example.myfirstapp.presenter.UserPresenter
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.Methods
import com.example.myfirstapp.utils.SimpleTextWatcher
import com.example.myfirstapp.utils.getString
import com.example.myfirstapp.utils.setColorBackground
import kotlinx.android.synthetic.main.activity_login.*
import java.io.Serializable
import javax.inject.Inject

class LoginActivity : RipleyBaseActivity(), UserPresenter.View {

    @Inject
    lateinit var userPresenter: UserPresenter

    override fun getView(): Int = R.layout.activity_login

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        userPresenter.attachView(this)

        txt_input_user.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                cleanError(txt_error_user, txt_input_user)
            }
        })

        txt_input_password.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                cleanError(txt_error_password, txt_input_password)
            }
        })

        btnStartShop.setOnClickListener {
            userPresenter.login(LoginRequest().apply {
                this.username = txt_input_user.getString()
                this.password = txt_input_password.getString()
                this.version = BuildConfig.VERSION_NAME
                this.session = Methods.generateRandomString()
            })
        }
     }

    override fun onResume() {
        userPresenter.attachView(this)
        super.onResume()
    }

    override fun onPause() {
        userPresenter.detachView()
        super.onPause()
    }

    fun cleanError(viewError: View, viewBackground: View) {
        viewError.visibility = View.GONE
        viewBackground.setBackgroundResource(R.drawable.shape_input_text)
        validButton()
    }

    private fun validButton() {
        val validation = ( txt_input_user.getString().isNotEmpty() && txt_input_password.getString().isNotEmpty())
        btnStartShop.isEnabled = validation
        btnStartShop.isClickable = validation
        btnStartShop.isFocusable = validation
        btnStartShop.setColorBackground(
            validation,
            this,
            R.color.colorPrimary,
            R.color.colorPrimaryOpa
        )
    }

    override fun userSuccessPresenter(status: Int, vararg args: Serializable) {
        when(status) {
            200 -> {

            }
            201 -> {
                
            }
        }
    }


}