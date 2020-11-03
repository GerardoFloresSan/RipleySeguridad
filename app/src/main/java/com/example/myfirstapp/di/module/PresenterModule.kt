package com.example.myfirstapp.di.module

import com.example.myfirstapp.di.PresenterScope
import com.example.myfirstapp.domain.useCase.*
import com.example.myfirstapp.presenter.*
import com.example.myfirstapp.utils.Methods
import dagger.Module
import dagger.Provides

@Module
@PresenterScope
class PresenterModule {

    @Provides
    @PresenterScope
    fun userPresenter(
        useCase: GetUser,
        methods: Methods
    ) = UserPresenter(useCase, methods)

    @Provides
    @PresenterScope
    fun salesPresenter(
        useCase: GetSalesQr,
        useCase2: GetSalesByDoc,
        useCase3: CloseSales,
        methods: Methods
    ) = SalesPresenter(useCase, useCase2, useCase3, methods)
}