package com.example.myfirstapp.data.request

import com.google.gson.annotations.SerializedName

data class CloseCartRequest (

    @SerializedName("orderId") val orderId : Int = 0,
    @SerializedName("hashQr") val hashQr : String = "",
    @SerializedName("clientSignature") val clientSignature : String = "",
    @SerializedName("username") val username : Int = 0,
    @SerializedName("token") val token : String = ""
)