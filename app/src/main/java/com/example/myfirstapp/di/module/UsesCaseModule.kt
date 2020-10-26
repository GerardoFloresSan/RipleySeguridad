package com.example.myfirstapp.di.module

import com.example.myfirstapp.data.repository.CheckRepository
import com.example.myfirstapp.data.repository.ParameterRepository
import com.example.myfirstapp.data.repository.SubsidiaryRepository
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
    fun checkPrice(
        repository: CheckRepository,
        @Named("executor_thread") executorThread: Scheduler,
        @Named("ui_thread") uiThread: Scheduler
    ) = CheckPrice(executorThread, uiThread, repository)

    @Provides
    @Singleton
    fun getParameters(
        repository: ParameterRepository,
        @Named("executor_thread") executorThread: Scheduler,
        @Named("ui_thread") uiThread: Scheduler
    ) = GetParameters(executorThread, uiThread, repository)

    @Provides
    @Singleton
    fun getSubsidiary(
        repository: SubsidiaryRepository,
        @Named("executor_thread") executorThread: Scheduler,
        @Named("ui_thread") uiThread: Scheduler
    ) = GetSubsidiary(executorThread, uiThread, repository)

    @Provides
    @Singleton
    fun getUser(
        repository: UserRepository,
        @Named("executor_thread") executorThread: Scheduler,
        @Named("ui_thread") uiThread: Scheduler
    ) = GetUser(executorThread, uiThread, repository)

    /** always in bottom */
    @Provides
    @Named("executor_thread")
    fun provideExecutorThread(): Scheduler = Schedulers.io()

    @Provides
    @Named("ui_thread")
    fun provideUiThread(): Scheduler = AndroidSchedulers.mainThread()

}