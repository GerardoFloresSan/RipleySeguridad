package com.example.myfirstapp.di.componet

import com.example.myfirstapp.di.PresenterScope
import com.example.myfirstapp.di.module.PresenterModule
import com.example.myfirstapp.ui.activity.*
import com.example.myfirstapp.ui.activity.checkout.PersonalInformationActivity
import dagger.Component

@PresenterScope
@Component(dependencies = [AppComponent::class], modules = [PresenterModule::class])
interface PresenterComponent {
    /*fun inject(fragment: ExampleFragment)*/
    fun inject(activity: MainActivity)
    fun inject(activity: SplashActivity)
    fun inject(activity: WelcomeActivity)
    fun inject(activity: ScanActivity)
    fun inject(activity: ShoppingCartActivity)
    fun inject(activity: CuponScanActivity)
    fun inject(activity: PersonalInformationActivity)
    //
}
