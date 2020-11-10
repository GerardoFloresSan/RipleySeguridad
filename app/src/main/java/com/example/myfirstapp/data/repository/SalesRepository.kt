package com.example.myfirstapp.data.repository

import com.example.myfirstapp.data.request.CloseCartRequest
import com.example.myfirstapp.data.request.GetStateByDocRequest
import com.example.myfirstapp.data.request.GetStateByQrRequest
import com.example.myfirstapp.data.response.CloseCartResponse
import com.example.myfirstapp.data.response.SalesGetByResponse
import com.example.myfirstapp.data.retrofit.ApiService
import com.example.myfirstapp.utils.Methods
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SalesRepository(var apiService: ApiService, var methods: Methods) {

    fun getStateByQr(request: GetStateByQrRequest): Observable<SalesGetByResponse> {
        val headers = mapOf(
            "Content-Type" to "application/json",
            "x-api-key" to "koXPLPvtXK8BKzi4tHkdk3h1JA6yVr5K4w5tr6eX")

        return apiService.getStateByQrV2(headers, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getStateByDoc(request: GetStateByDocRequest): Observable<List<SalesGetByResponse>> {
        val headers = mapOf(
            "Content-Type" to "application/json",
            "x-api-key" to "koXPLPvtXK8BKzi4tHkdk3h1JA6yVr5K4w5tr6eX")

        return apiService.getStateByDocV2(headers, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun salesCloseCart(request: CloseCartRequest): Observable<CloseCartResponse > {
        val headers = mapOf(
            "Content-Type" to "application/json",
            "x-api-key" to "koXPLPvtXK8BKzi4tHkdk3h1JA6yVr5K4w5tr6eX")

        return apiService.salesCloseCartV2(headers, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}