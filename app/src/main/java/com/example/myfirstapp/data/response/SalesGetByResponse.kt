package com.example.myfirstapp.data.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SalesGetByResponse (
    @SerializedName("orderId") var orderId : Int = 0,
    @SerializedName("statusId") var statusId : Int = 0,
    @SerializedName("status") var status : String = "",
    @SerializedName("subsidiary") var subsidiary : Int = 0,
    @SerializedName("subsidiaryName") var subsidiaryName : String = "",
    @SerializedName("totalAmount") var totalAmount : Int = 0,
    @SerializedName("trxNumber") var trxNumber : String = "",
    @SerializedName("date") var date : String = "",
    @SerializedName("clientDoc") var clientDoc : Int = 0,
    @SerializedName("clientName") var clientName : String = "",
    @SerializedName("clientLast") var clientLast : String = "",
    @SerializedName("clientEmail") var clientEmail : String = "",
    @SerializedName("clientTel") var clientTel : Int = 0,
    @SerializedName("indRipley") var indRipley : Int = 0,
    @SerializedName("coupons") var coupons : ArrayList<String> = arrayListOf(),
    @SerializedName("cardNumber") var cardNumber : String = "",
    @SerializedName("cardBrand") var cardBrand : String = "",
    @SerializedName("hashQr") var hashQr : String = "",
    @SerializedName("products") var products :ArrayList<Product> = arrayListOf()
) : Serializable {
    data class Product (
        @SerializedName("sku") var sku : String = "",
        @SerializedName("quantity") var quantity : Int = 0,
        @SerializedName("description") var description : String = "",
        @SerializedName("price") var price : Int = 0
    ) : Serializable
}