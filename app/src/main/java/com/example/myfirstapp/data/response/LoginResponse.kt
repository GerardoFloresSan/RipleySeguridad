package com.example.myfirstapp.data.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class LoginResponse (
    @SerializedName("subsidiary") var subsidiary : String = "",
    @SerializedName("subsidiaryName") var subsidiaryName : String = "",
    @SerializedName("document") var document : String = "",
    @SerializedName("name") var name : String = "",
    @Suppress("SpellCheckingInspection") @SerializedName("lastname") val lastName : String = "",
    @SerializedName("token") var token : String = ""
) : Serializable