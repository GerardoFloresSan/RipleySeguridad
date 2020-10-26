package com.example.myfirstapp.ui.adapter.carouselview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.myfirstapp.R
import kotlinx.android.synthetic.main.fragment_carousel_page.*

class CarouselPageFragment(private val page: CarouselPage) : Fragment(R.layout.fragment_carousel_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        carouselPageImage.setImageResource(page.image)
        carouselPageTitle.setText(page.title)
        carouselPageText.setText(page.text)
    }
}