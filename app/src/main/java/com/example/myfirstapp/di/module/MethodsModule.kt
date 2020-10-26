package com.example.myfirstapp.di.module

import android.content.Context
import com.example.myfirstapp.utils.Methods
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MethodsModule {

    @Provides
    @Singleton
    fun provideMethods(context: Context) = Methods(context)
}