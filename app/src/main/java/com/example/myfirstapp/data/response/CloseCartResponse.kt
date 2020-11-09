package com.example.myfirstapp.data.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CloseCartResponse(
    @SerializedName("clientVoucher") var clientVoucher: ArrayList<ClientVoucher> = arrayListOf()
) : Serializable {
    data class ClientVoucher(
        @SerializedName("text") var text: String = "",
        @SerializedName("align") var align: String = "",
        @SerializedName("tipo") var tipo: String = "",
        @SerializedName("text1") var text1: String = "",
        @SerializedName("text2") var text2: String = "",
        @SerializedName("sizeColumn1") var sizeColumn1: String = "",
        @SerializedName("sizeColumn3") var sizeColumn3: String = "",
        @SerializedName("textLeft") var textLeft: String = "",
        @SerializedName("textRight") var textRight: String = ""

    ) : Serializable
}