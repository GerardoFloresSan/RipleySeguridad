package com.example.myfirstapp.data.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetStateByQrRequest (
    @SerializedName("hashQr") var hashQr : String = "",
    @SerializedName("username") var username : String = "",
    @SerializedName("token") var token : String = ""
) : Serializable