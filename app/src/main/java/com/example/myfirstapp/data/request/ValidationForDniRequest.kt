package com.example.myfirstapp.data.request

import com.google.gson.annotations.SerializedName

data class ValidationForDniRequest (

    @SerializedName("clientDoc") val clientDoc : Int = 0,
    @SerializedName("username") val username : Int = 0,
    @SerializedName("token") val token : String = ""
)