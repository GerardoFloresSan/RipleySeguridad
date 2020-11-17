package com.example.myfirstapp.di.componet

import android.content.Context
import com.example.myfirstapp.data.retrofit.ApiService
import com.example.myfirstapp.di.module.*
import com.example.myfirstapp.domain.useCase.*
import com.example.myfirstapp.utils.Methods
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, MethodsModule::class, UsesCaseModule::class, RepositoryModule::class])
interface AppComponent {

    fun context(): Context

    fun apiService(): ApiService

    fun methods(): Methods
    /**use case */
    fun getUser(): GetUser
    fun getParameters(): GetParameters
    fun getSalesQr(): GetSalesQr
    fun getSalesByDoc(): GetSalesByDoc
    fun closeSales(): CloseSales
}
