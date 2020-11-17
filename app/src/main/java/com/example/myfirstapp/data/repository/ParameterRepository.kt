package com.example.myfirstapp.data.repository

import com.example.myfirstapp.BuildConfig
import com.example.myfirstapp.data.response.Parameter
import com.example.myfirstapp.data.retrofit.ApiService
import com.example.myfirstapp.utils.Methods
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ParameterRepository(var apiService: ApiService, var methods: Methods) {
    //private val futureMapper = FutureMapper()

    fun getParametersAll(): Observable<List<Parameter>> {
        val headers = mapOf(
            "x-api-key" to BuildConfig.API_PARAM)

        return apiService.getParameterAll(headers)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        /*.observeOn(AndroidSchedulers.mainThread()).map { input ->
            futureMapper.reverseMap(input)
        }*/
    }
}