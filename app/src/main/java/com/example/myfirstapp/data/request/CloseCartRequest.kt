package com.example.myfirstapp.data.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CloseCartRequest (
    @SerializedName("orderId") var orderId : Int = 0,
    @SerializedName("hashQr") var hashQr : String = "",
    @SerializedName("clientSignature") var clientSignature : String = "",
    @SerializedName("username") var username : Int = 0,
    @SerializedName("token") var token : String = ""
) : Serializable