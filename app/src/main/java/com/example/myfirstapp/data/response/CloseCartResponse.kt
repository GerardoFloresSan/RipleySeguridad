package com.example.myfirstapp.data.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CloseCartResponse (
    @SerializedName("clientVoucher") var clientVoucher : ArrayList<ClientVoucher> = arrayListOf()
) : Serializable{
    data class ClientVoucher (
        @SerializedName("text") var text : String = "",
        @SerializedName("align") var align : String = "",
        @SerializedName("tipo") var tipo : String = ""
    ) : Serializable
}