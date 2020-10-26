package com.example.myfirstapp.data.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CheckPricesRequest (
    @SerializedName("subsidiary") var subsidiary: String = "",
    @SerializedName("products") var products: ArrayList<Product> = arrayListOf(),
    @SerializedName("coupons") var coupons: ArrayList<String> = arrayListOf(),
    @SerializedName("session") var session: String = ""
) : Serializable {
    data class Product (
        @SerializedName("id") var id: Int = 0,
        @SerializedName("sku") var sku: String = "",
        @SerializedName("quantity") var quantity: Int = 0
    )
}