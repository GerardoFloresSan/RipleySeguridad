package com.example.myfirstapp.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myfirstapp.R
import com.example.myfirstapp.ui.adapter.carouselview.CarouselPage
import com.example.myfirstapp.ui.adapter.carouselview.CarouselPager
import com.example.myfirstapp.ui.adapter.carouselview.ZoomOutPageTransformer
import com.example.myfirstapp.ui.application.RipleyApplication
import com.example.myfirstapp.ui.base.RipleyBaseActivity
import com.example.myfirstapp.utils.startActivityE
import kotlinx.android.synthetic.main.activity_on_boarding.*

class OnBoardingActivity : RipleyBaseActivity(), CarouselPager.CarouselListener {

    private val carouselPages = listOf(
        CarouselPage(
            R.drawable.ic_onboarding_one,
            R.string.onBoarding1_title,
            R.string.onBoarding1_text
        ),
        CarouselPage(
            R.drawable.ic_onboarding_two,
            R.string.onBoarding2_title,
            R.string.onBoarding2_text
        ),
        CarouselPage(
            R.drawable.ic_onboarding_three,
            R.string.onBoarding3_title,
            R.string.onBoarding3_text
        )
    )

    override fun getView(): Int = R.layout.activity_on_boarding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        carouselPager.setUpCarousel(this, carouselPages)
            .setUpPageTransformer(ZoomOutPageTransformer())
            .setUpCarouselListener(this)
    }

    override fun onCarouselFinished(skipped: Boolean) {
        if (skipped) {
            if(checkPermissionsCamera()) {
                finish()
                startActivityE(ScanActivity::class.java)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getCameraPermission()
                }
            }
        }//
        else {
            if(checkPermissionsCamera()) {
                finish()
                startActivityE(ScanActivity::class.java)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getCameraPermission()
                }
            }
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            PERMISSION_CAMERA ->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    finish()
                    startActivityE(ScanActivity::class.java)
                } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    toast("Que es obligatorio el permiso de la cámara para poder continuar")
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            toast("Que es obligatorio el permiso de la cámara para poder continuar")
                        }
                    }*/
                }
            }
            else -> {
                toast("Que es obligatorio el permiso de la cámara para poder continuar")
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        RipleyApplication.closeAll()
    }

}