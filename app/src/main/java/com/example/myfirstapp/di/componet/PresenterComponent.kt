package com.example.myfirstapp.di.componet

import com.example.myfirstapp.di.PresenterScope
import com.example.myfirstapp.di.module.PresenterModule
import com.example.myfirstapp.ui.activity.security.*
import com.example.myfirstapp.ui.activity.seguridad.FirmActivity
import dagger.Component

@PresenterScope
@Component(dependencies = [AppComponent::class], modules = [PresenterModule::class])
interface PresenterComponent {
    /*fun inject(fragment: ExampleFragment)*/
    fun inject(activity: LoginActivity)
    fun inject(activity: DetailShopActivity)
    fun inject(activity: ValidationActivity)
    fun inject(activity: FirmActivity)
    //
}
