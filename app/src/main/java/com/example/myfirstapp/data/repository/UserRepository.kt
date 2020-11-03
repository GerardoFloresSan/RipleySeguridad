package com.example.myfirstapp.data.repository

import com.example.myfirstapp.data.request.LoginRequest
import com.example.myfirstapp.data.response.LoginResponse
import com.example.myfirstapp.data.retrofit.ApiService
import com.example.myfirstapp.utils.Methods
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserRepository(var apiService: ApiService, var methods: Methods) {

    fun login(loginRequest: LoginRequest): Observable<LoginResponse> {
        val url = "https://api-ripleymobile-qa.ripley.com.pe/api/sales/login"
        val headers = mapOf(
            "Content-Type" to "application/json",
            "x-api-key" to "e3Z23YLIid93Z7K8bXXGY2MLrAPLHo3w8B9N3MXp")

        return apiService.login(url, headers, loginRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}