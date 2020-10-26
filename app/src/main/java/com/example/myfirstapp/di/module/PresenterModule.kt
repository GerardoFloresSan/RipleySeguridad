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
    fun subsidiaryPresenter(
        useCase: GetSubsidiary,
        methods: Methods
    ) = SubsidiaryPresenter(useCase, methods)

    @Provides
    @PresenterScope
    fun parameterPresenter(
        useCase: GetParameters,
        methods: Methods
    ) = ParameterPresenter(useCase, methods)

    @Provides
    @PresenterScope
    fun checkPricePresenter(
        useCase: CheckPrice,
        methods: Methods
    ) = CheckPricePresenter(useCase, methods)
}