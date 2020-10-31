package com.example.myfirstapp.data.response

import com.example.myfirstapp.data.request.CheckPricesRequest
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ValidationForDniResponse (

    @SerializedName("orderId") val orderId : Int = 0,
    @SerializedName("statusId") val statusId : Int = 0,
    @SerializedName("status") val status : String = "",
    @SerializedName("subsidiary") val subsidiary : Int = 0,
    @SerializedName("subsidiaryName") val subsidiaryName : String = "",
    @SerializedName("totalAmount") val totalAmount : Int = 0,
    @SerializedName("trxNumber") val trxNumber : String = "",
    @SerializedName("date") val date : String = "",
    @SerializedName("clientDoc") val clientDoc : Int = 0,
    @SerializedName("clientName") val clientName : String = "",
    @SerializedName("clientLast") val clientLast : String = "",
    @SerializedName("clientEmail") val clientEmail : String = "",
    @SerializedName("clientTel") val clientTel : Int = 0,
    @SerializedName("indRipley") val indRipley : Int = 0,
    @SerializedName("coupons") val coupons : ArrayList<String> = arrayListOf(),
    @SerializedName("cardNumber") val cardNumber : String = "",
    @SerializedName("cardBrand") val cardBrand : String = "",
    @SerializedName("hashQr") val hashQr : String = "",
    @SerializedName("products") val products :ArrayList<CheckPricesRequest.Product> = arrayListOf()
) : Serializable {
    data class Products (
        @SerializedName("sku") val sku : Int = 0,
        @SerializedName("quantity") val quantity : Int = 0,
        @SerializedName("description") val description : String = "",
        @SerializedName("price") val price : Int = 0
    )
}