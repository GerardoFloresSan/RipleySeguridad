package com.example.myfirstapp.data.repository

import com.example.myfirstapp.data.request.CheckPricesRequest
import com.example.myfirstapp.data.response.CheckPricesResponse
import com.example.myfirstapp.data.response.User
import com.example.myfirstapp.data.retrofit.ApiService
import com.example.myfirstapp.utils.Methods
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CheckRepository(var apiService: ApiService, var methods: Methods) {
    //private val futureMapper = FutureMapper()

    fun checkPrices(checkPricesRequest: CheckPricesRequest): Observable<CheckPricesResponse> {
        val url = "http://qaalbschapi-1055955990.us-east-1.elb.amazonaws.com/checkprices/check" // QA
        val urlPro = "http://prdalbschapi-1027184916.ca-central-1.elb.amazonaws.com/checkprices/check" // PROD
        val headers = mapOf(
            "Content-Type" to "application/json",
            "x-api-key" to "e3Z23YLIid93Z7K8bXXGY2MLrAPLHo3w8B9N3MXp")

        return apiService.checkQa(url, headers, checkPricesRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        /*.observeOn(AndroidSchedulers.mainThread()).map { input ->
            futureMapper.reverseMap(input)
        }*/
    }
}