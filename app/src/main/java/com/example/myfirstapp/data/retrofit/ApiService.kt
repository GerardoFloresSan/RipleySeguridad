package com.example.myfirstapp.data.retrofit

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
    ): Observable<SalesGetStateResponse>

    @POST
    fun getStateByDoc(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @Body request: GetStateByDocRequest
    ): Observable<List<SalesGetByDocResponse>>

    @POST
    fun salesCloseCart(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @Body request: CloseCartRequest
    ): Observable<CloseCartResponse>
}