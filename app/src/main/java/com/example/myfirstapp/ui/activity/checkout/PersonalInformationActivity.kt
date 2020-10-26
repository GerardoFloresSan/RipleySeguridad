package com.example.myfirstapp.ui.activity.checkout

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.util.Patterns
import com.example.myfirstapp.R
import com.example.myfirstapp.data.response.User
import com.example.myfirstapp.presenter.UserPresenter
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.*
import kotlinx.android.synthetic.main.activity_personal_information.*
import java.io.Serializable
import javax.inject.Inject

class PersonalInformationActivity : RipleyBaseActivity(), UserPresenter.View {

    @Inject
    lateinit var userPresenter: UserPresenter
    var waitConsult: Boolean = true

    override fun getView(): Int = R.layout.activity_personal_information

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        userPresenter.attachView(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvw_terms_check.text = Html.fromHtml("<font color=#2F2F2F>Aceptar </font><font color=#70578B>  <u>Términos y Condiciones.</u>  </font>", 0)
        } else {
            tvw_terms_check.text = Html.fromHtml("<font color=#2F2F2F>Aceptar </font><font color=#70578B>  <u>Términos y Condiciones.</u>  </font>")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvw_privacy_check.text = Html.fromHtml("<font color=#2F2F2F>Aceptar </font><font color=#70578B>  <u>Política de Tratamientos de Datos Personales.</u>  </font>", 0)
        } else {
            tvw_privacy_check.text = Html.fromHtml("<font color=#2F2F2F>Aceptar </font><font color=#70578B>  <u>Política de Tratamientos de Datos Personales.</u>  </font>")
        }

        btn_back.setOnClickListener {
            finish()
        }
        btn_next_personal_info.setOnClickListener {
            //TODO ADD VALIDATION EMAIL ANG PHONE
            startActivityE(CheckOutActivity::class.java)
        }
        tvw_terms_check.setOnClickListener {
            val t = Html.fromHtml(Methods.getParameter(65).value)
            customInfo(t) {}
        }
        tvw_privacy_check.setOnClickListener {
            val t = Html.fromHtml(Methods.getParameter(107).value)
            customInfo(t) {}
        }

        txt_input_number_identification.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                if(s != null) {
                    if(s.length == 8) {
                        if(waitConsult) {
                            waitConsult = false
                            userPresenter.getUserByDoc(s.toString().trim())
                        }
                    } else {
                        waitConsult = true
                    }
                }
            }
        })

        txt_input_name.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                val temp = PapersManager.userLocal
                temp.apply {
                    this.first_name = s.toString().trim()
                }
                PapersManager.userLocal = temp
                validButton()
            }
        })

        txt_input_lastname.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                val temp = PapersManager.userLocal
                temp.apply {
                    this.last_name = s.toString().trim()
                }
                PapersManager.userLocal = temp
                validButton()
            }
        })

        txt_input_email.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                val temp = PapersManager.userLocal
                temp.apply {
                    this.email = s.toString().trim()
                }
                PapersManager.userLocal = temp
                validButton()
            }
        })

        txt_input_cellphone.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                val temp = PapersManager.userLocal
                temp.apply {
                    this.phone = s.toString().trim()
                }
                PapersManager.userLocal = temp
                validButton()
            }
        })
        chk_terms_accept.setOnCheckedChangeListener { _, isChecked ->
            validButton()
        }
        chk_privacy_accept.setOnCheckedChangeListener { _, isChecked ->
            validButton()
        }
        Methods.getParameter(107)
    }

    fun validButton() {
        val validation = (
            txt_input_number_identification.getString().isNotEmpty() &&
            txt_input_name.getString().isNotEmpty() &&
            txt_input_lastname.getString().isNotEmpty() &&
            txt_input_email.getString().isNotEmpty() &&
            txt_input_cellphone.getString().isNotEmpty() &&
            chk_terms_accept.isChecked &&
            chk_privacy_accept.isChecked)

        btn_next_personal_info.isEnabled = validation
        btn_next_personal_info.isClickable = validation
        btn_next_personal_info.isFocusable = validation
        btn_next_personal_info.setColorBackground(validation, this, R.color.colorPrimary, R.color.colorPrimaryOpa)
    }

    private fun validateEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    override fun onResume() {
        super.onResume()
        userPresenter.attachView(this)
        with(PapersManager.userLocal){
            if(this.document_number.isNotEmpty()) {
                waitConsult = false
                txt_input_number_identification.setText(this.document_number)
                txt_input_name.setText(this.first_name)
                txt_input_lastname.setText(this.last_name)
                txt_input_email.setText(this.email)
                txt_input_cellphone.setText(this.phone)
            }
        }
        val shopping = PapersManager.shoppingCart
        if (shopping.products.isEmpty()) {
            txt_total_other_payed.text = "S/ 0.00"
            txt_total_shopping_car.text = "S/ 0.00"
        } else {
            txt_total_other_payed.text = Methods.formatMoney((shopping.totalPromo.toDouble() / 100))
            txt_total_shopping_car.text = Methods.formatMoney((shopping.totalRipley.toDouble() / 100))
        }
        validButton()
    }

    override fun onPause() {
        super.onPause()
        userPresenter.detachView()
    }

    override fun userSuccessPresenter(status: Int, vararg args: Serializable) {
        when(status) {
            200 -> {
                val user : User = args[0] as User
                with(user){
                    txt_input_number_identification.setText(this.document_number)
                    txt_input_name.setText(this.first_name)
                    txt_input_lastname.setText(this.last_name)
                    txt_input_email.setText(this.email)
                    txt_input_cellphone.setText(this.phone)
                }
                val temp = PapersManager.userLocal
                temp.apply {
                    this.document_number = user.document_number
                }
                PapersManager.userLocal = temp
                validButton()
            }
            else -> {
                toast("Error al consultar servicio")
            }
        }
    }
}