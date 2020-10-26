package com.example.myfirstapp.ui.activity.datacard

import android.os.Bundle
import com.example.myfirstapp.R
import com.example.myfirstapp.ui.activity.checkout.PersonalInformationActivity
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.startActivityE
import kotlinx.android.synthetic.main.activity_data_card.*

class DataCardActivity : RipleyBaseActivity() {

    override fun getView(): Int = R.layout.activity_data_card

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_pay.setOnClickListener {
            startActivityE(ErrorActivity::class.java)
        }
    }

}