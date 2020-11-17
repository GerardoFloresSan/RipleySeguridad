package com.example.myfirstapp.di.module

import com.example.myfirstapp.data.repository.ParameterRepository
import com.example.myfirstapp.data.repository.SalesRepository
import com.example.myfirstapp.data.repository.UserRepository
import com.example.myfirstapp.domain.useCase.*
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

@Module
class UsesCaseModule {

    @Provides
    @Singleton
    fun getUser(
        repository: UserRepository,
        @Named("executor_thread") executorThread: Scheduler,
        @Named("ui_thread") uiThread: Scheduler
    ) = GetUser(executorThread, uiThread, repository)

    @Provides
    @Singleton
    fun getSalesQr(
        repository: SalesRepository,
        @Named("executor_thread") executorThread: Scheduler,
        @Named("ui_thread") uiThread: Scheduler
    ) = GetSalesQr(executorThread, uiThread, repository)

    @Provides
    @Singleton
    fun getParameters(
        repository: ParameterRepository,
        @Named("executor_thread") executorThread: Scheduler,
        @Named("ui_thread") uiThread: Scheduler
    ) = GetParameters(executorThread, uiThread, repository)

    @Provides
    @Singleton
    fun getSalesByDoc(
        repository: SalesRepository,
        @Named("executor_thread") executorThread: Scheduler,
        @Named("ui_thread") uiThread: Scheduler
    ) = GetSalesByDoc(executorThread, uiThread, repository)

    @Provides
    @Singleton
    fun closeSales(
        repository: SalesRepository,
        @Named("executor_thread") executorThread: Scheduler,
        @Named("ui_thread") uiThread: Scheduler
    ) = CloseSales(executorThread, uiThread, repository)

    /** always in bottom */
    @Provides
    @Named("executor_thread")
    fun provideExecutorThread(): Scheduler = Schedulers.io()

    @Provides
    @Named("ui_thread")
    fun provideUiThread(): Scheduler = AndroidSchedulers.mainThread()

}