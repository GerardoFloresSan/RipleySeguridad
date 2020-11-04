package com.example.myfirstapp.data.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginRequest (
    @SerializedName("username") var username : String = "",
    @SerializedName("password") var password : String = "",
    @SerializedName("version") var version : String = "",
    @SerializedName("session") var session : String = ""
) : Serializable