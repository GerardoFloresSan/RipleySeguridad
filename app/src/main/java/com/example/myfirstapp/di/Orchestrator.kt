package com.example.myfirstapp.di

import com.example.myfirstapp.di.componet.DaggerPresenterComponent
import com.example.myfirstapp.di.componet.PresenterComponent
import com.example.myfirstapp.ui.application.RipleyApplication

object Orchestrator {

    val presenterComponent: PresenterComponent by lazy {
        DaggerPresenterComponent
            .builder()
            .appComponent(RipleyApplication.appComponent)
            .build()
    }
}
