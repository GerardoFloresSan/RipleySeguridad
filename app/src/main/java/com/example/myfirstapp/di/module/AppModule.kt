package com.example.myfirstapp.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule (val context: Context){

    @Provides
    @Singleton
    fun providesContext() = context
}