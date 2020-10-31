package com.example.myfirstapp.data.response

import com.google.gson.annotations.SerializedName


data class LoginResponse (

    @SerializedName("subsidiary") val subsidiary : Int,
    @SerializedName("subsidiaryName") val subsidiaryName : String,
    @SerializedName("document") val document : Int,
    @SerializedName("name") val name : String,
    @SerializedName("lastname") val lastname : String,
    @SerializedName("token") val token : String
)