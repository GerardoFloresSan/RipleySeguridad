package com.example.myfirstapp.data.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CloseCartResponse (
    @SerializedName("clientVoucher") val clientVoucher : List<ClientVoucher>
) : Serializable{
    data class ClientVoucher (

        @SerializedName("text") val text : String = "",
        @SerializedName("align") val align : String = "",
        @SerializedName("tipo") val tipo : String = ""
    )
}