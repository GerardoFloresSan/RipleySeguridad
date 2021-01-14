package com.example.myfirstapp.data.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CloseCartRequest (
    @SerializedName("orderId") var orderId : String = "",
    @SerializedName("hashQr") var hashQr : String = "",
    @SerializedName("clientSignature") var clientSignature : String = "",
    @SerializedName("username") var username : String = "",
    @SerializedName("latitude") var latitude: Double = 0.0,
    @SerializedName("longitude") var longitude: Double = 0.0,
    @SerializedName("token") var token : String = ""
) : Serializable