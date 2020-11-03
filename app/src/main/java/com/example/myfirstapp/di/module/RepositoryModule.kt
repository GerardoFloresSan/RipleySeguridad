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
    fun salesRepository(apiService: ApiService, methods: Methods) = SalesRepository(apiService, methods)

}