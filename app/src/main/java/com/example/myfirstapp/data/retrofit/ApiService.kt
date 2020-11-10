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

    @POST
    fun getStateByQr(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @Body request: GetStateByQrRequest
    ): Observable<SalesGetByResponse>

    @POST
    fun getStateByDoc(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @Body request: GetStateByDocRequest
    ): Observable<List<SalesGetByResponse>>

    @POST
    fun salesCloseCart(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @Body request: CloseCartRequest
    ): Observable<CloseCartResponse>


    //USE URL_BASE AND URL_TRACE
    @POST(BuildConfig.URL_BASE_TRACE + "sales/login")
    fun loginV2(
        @HeaderMap headers: Map<String, String>,
        @Body request: LoginRequest
    ): Observable<LoginResponse>

    @POST(BuildConfig.URL_BASE_TRACE + "sales/getstate")
    fun getStateByQrV2(
        @HeaderMap headers: Map<String, String>,
        @Body request: GetStateByQrRequest
    ): Observable<SalesGetByResponse>

    @POST(BuildConfig.URL_BASE_TRACE + "sales/getbydni")
    fun getStateByDocV2(
        @HeaderMap headers: Map<String, String>,
        @Body request: GetStateByDocRequest
    ): Observable<List<SalesGetByResponse>>

    @POST(BuildConfig.URL_BASE_TRACE + "sales/closecart")
    fun salesCloseCartV2(
        @HeaderMap headers: Map<String, String>,
        @Body request: CloseCartRequest
    ): Observable<CloseCartResponse>
}