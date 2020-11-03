package com.example.myfirstapp.data.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetStateByDocRequest (
    @SerializedName("clientDoc") var clientDoc : Int = 0,
    @SerializedName("username") var username : Int = 0,
    @SerializedName("token") var token : String = ""
) : Serializable