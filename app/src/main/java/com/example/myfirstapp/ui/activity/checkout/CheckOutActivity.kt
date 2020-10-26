package com.example.myfirstapp.ui.activity.checkout

import android.os.Bundle
import com.example.myfirstapp.R
import com.example.myfirstapp.ui.activity.datacard.DataCardActivity
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.startActivityE
import kotlinx.android.synthetic.main.activity_check_out.*

class CheckOutActivity : RipleyBaseActivity() {

    override fun getView(): Int = R.layout.activity_check_out

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        btn_next_check_out.setOnClickListener {
            startActivityE(DataCardActivity::class.java)
        }

    }


}