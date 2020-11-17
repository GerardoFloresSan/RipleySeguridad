package com.example.myfirstapp.data.repository

import com.example.myfirstapp.BuildConfig
import com.example.myfirstapp.data.request.LoginRequest
import com.example.myfirstapp.data.response.LoginResponse
import com.example.myfirstapp.data.retrofit.ApiService
import com.example.myfirstapp.utils.Methods
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserRepository(var apiService: ApiService, var methods: Methods) {

    fun login(loginRequest: LoginRequest): Observable<LoginResponse> {
        val headers = mapOf(
            "Content-Type" to "application/json",
            "x-api-key" to BuildConfig.API_KEY)

        return apiService.loginV2(headers, loginRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}