package com.example.myfirstapp.data.repository

import com.example.myfirstapp.data.request.CloseCartRequest
import com.example.myfirstapp.data.request.GetStateByDocRequest
import com.example.myfirstapp.data.request.GetStateByQrRequest
import com.example.myfirstapp.data.response.CloseCartResponse
import com.example.myfirstapp.data.response.SalesGetByDocResponse
import com.example.myfirstapp.data.response.SalesGetStateResponse
import com.example.myfirstapp.data.retrofit.ApiService
import com.example.myfirstapp.utils.Methods
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SalesRepository(var apiService: ApiService, var methods: Methods) {

    fun getStateByQr(request: GetStateByQrRequest): Observable<SalesGetStateResponse> {
        val url = "https://api-ripleymobile-qa.ripley.com.pe/api/sales/getstate"
        val headers = mapOf(
            "Content-Type" to "application/json",
            "x-api-key" to "e3Z23YLIid93Z7K8bXXGY2MLrAPLHo3w8B9N3MXp")

        return apiService.getStateByQr(url, headers, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getStateByDoc(request: GetStateByDocRequest): Observable<List<SalesGetByDocResponse>> {
        val url = "https://api-ripleymobile-qa.ripley.com.pe/api/sales/getbydni"
        val headers = mapOf(
            "Content-Type" to "application/json",
            "x-api-key" to "e3Z23YLIid93Z7K8bXXGY2MLrAPLHo3w8B9N3MXp")

        return apiService.getStateByDoc(url, headers, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun salesCloseCart(request: CloseCartRequest): Observable<CloseCartResponse > {
        val url = "https://api-ripleymobile-qa.ripley.com.pe/api/sales/login"
        val headers = mapOf(
            "Content-Type" to "application/json",
            "x-api-key" to "e3Z23YLIid93Z7K8bXXGY2MLrAPLHo3w8B9N3MXp")

        return apiService.salesCloseCart(url, headers, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}