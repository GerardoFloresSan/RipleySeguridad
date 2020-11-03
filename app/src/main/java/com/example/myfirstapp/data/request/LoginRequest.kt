package com.example.myfirstapp.data.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginRequest (
    @SerializedName("username") val username : Int = 0,
    @SerializedName("password") val password : String = "",
    @SerializedName("version") val version : String = "",
    @SerializedName("session") val session : String = ""
) : Serializable