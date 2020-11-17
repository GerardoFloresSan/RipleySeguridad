package com.example.myfirstapp.data.retrofit

import com.example.myfirstapp.BuildConfig
import com.example.myfirstapp.data.request.*
import com.example.myfirstapp.data.response.*
import io.reactivex.Observable
import retrofit2.http.*

interface ApiService {
    @POST
    fun login(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @Body request: LoginRequest
    ): Observable<LoginResponse>

    @GET(BuildConfig.URL_GetParameterAll)
    fun getParameterAll(
        @HeaderMap headers: Map<String, String>
    ): Observable<List<Parameter>>

    //USE URL_BASE AND URL_TRACE
    @POST(BuildConfig.URL_LOGIN)
    fun loginV2(
        @HeaderMap headers: Map<String, String>,
        @Body request: LoginRequest
    ): Observable<LoginResponse>

    @POST(BuildConfig.URL_GETSTATE)
    fun getStateByQrV2(
        @HeaderMap headers: Map<String, String>,
        @Body request: GetStateByQrRequest
    ): Observable<SalesGetByResponse>

    @POST(BuildConfig.URL_GETDNI)
    fun getStateByDocV2(
        @HeaderMap headers: Map<String, String>,
        @Body request: GetStateByDocRequest
    ): Observable<List<SalesGetByResponse>>

    @POST(BuildConfig.URL_CLOSECART)
    fun salesCloseCartV2(
        @HeaderMap headers: Map<String, String>,
        @Body request: CloseCartRequest
    ): Observable<CloseCartResponse>
}