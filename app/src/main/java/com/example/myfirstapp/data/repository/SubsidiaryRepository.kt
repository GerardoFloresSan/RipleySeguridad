package com.example.myfirstapp.data.repository

import com.example.myfirstapp.data.response.Subsidiary
import com.example.myfirstapp.data.response.User
import com.example.myfirstapp.data.retrofit.ApiService
import com.example.myfirstapp.utils.Methods
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SubsidiaryRepository(var apiService: ApiService, var methods: Methods) {
    //private val futureMapper = FutureMapper()

    fun getSubsidiaries(): Observable<List<Subsidiary>> {
        val url = "https://ux9wpccub6.execute-api.us-east-1.amazonaws.com/qa/api/parameters/getSubsidiaryAllApp"
        val headers = mapOf(
            "Content-Type" to "application/json",
            "x-api-key" to "e3Z23YLIid93Z7K8bXXGY2MLrAPLHo3w8B9N3MXp")

        return apiService.getSubsidiaryAllApp(url, headers)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        /*.observeOn(AndroidSchedulers.mainThread()).map { input ->
            futureMapper.reverseMap(input)
        }*/
    }
}