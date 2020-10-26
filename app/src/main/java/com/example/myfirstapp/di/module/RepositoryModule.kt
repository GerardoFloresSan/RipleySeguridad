package com.example.myfirstapp.di.module

import com.example.myfirstapp.data.repository.*
import com.example.myfirstapp.data.retrofit.ApiService
import com.example.myfirstapp.utils.Methods
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun userRepository(apiService: ApiService, methods: Methods) = UserRepository(apiService, methods)

    @Provides
    @Singleton
    fun checkRepository(apiService: ApiService, methods: Methods) = CheckRepository(apiService, methods)

    @Provides
    @Singleton
    fun subsidiaryRepository(apiService: ApiService, methods: Methods) = SubsidiaryRepository(apiService, methods)

    @Provides
    @Singleton
    fun parameterRepository(apiService: ApiService, methods: Methods) = ParameterRepository(apiService, methods)
}