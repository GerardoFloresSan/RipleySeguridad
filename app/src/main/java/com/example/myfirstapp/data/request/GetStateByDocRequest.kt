package com.example.myfirstapp.data.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetStateByDocRequest (
    @SerializedName("clientDoc") var clientDoc : String = "",
    @SerializedName("username") var username : String = "",
    @SerializedName("token") var token : String = ""
) : Serializable