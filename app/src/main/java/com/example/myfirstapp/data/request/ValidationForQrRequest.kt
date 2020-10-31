package com.example.myfirstapp.data.request

import com.google.gson.annotations.SerializedName

data class ValidationForQrRequest (

    @SerializedName("hashQr") val hashQr : String = "",
    @SerializedName("username") val username : Int = 0,
    @SerializedName("token") val token : String = ""
)