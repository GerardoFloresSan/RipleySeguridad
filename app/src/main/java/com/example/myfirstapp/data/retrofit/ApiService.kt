package com.example.myfirstapp.data.retrofit

import com.example.myfirstapp.data.request.CheckPricesRequest
import com.example.myfirstapp.data.response.*
import io.reactivex.Observable
import retrofit2.http.*

interface ApiService {

    @GET
    fun getUserByDni(
        @Url url: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<User>

    @GET
    fun getSubsidiaryAllApp(
        @Url url: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<List<Subsidiary>>

    @GET
    fun getParameterAll(
        @Url url: String,
        @HeaderMap headers: Map<String, String>
    ): Observable<List<Parameter>>

    @POST
    fun checkQa(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @Body request: CheckPricesRequest
    ): Observable<CheckPricesResponse>
}