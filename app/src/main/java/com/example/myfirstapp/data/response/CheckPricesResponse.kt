package com.example.myfirstapp.data.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CheckPricesResponse (
    @SerializedName("subsidiary") var subsidiary: String = "",
    @SerializedName("products") var products: ArrayList<Product> = arrayListOf(),
    @SerializedName("coupons") var coupons: ArrayList<String> = arrayListOf(),
    @SerializedName("session") var session: String = "",
    @SerializedName("totalPromo") var totalPromo: Long = 0,
    @SerializedName("totalRipley") var totalRipley: Long = 0
) : Serializable {
    data class Product (
        @SerializedName("id") var id: Long = 0,
        @SerializedName("sku") var sku: String = "",
        @SerializedName("quantity") var quantity: Long = 0,
        @SerializedName("codRipley") var  codRipley: String = "",
        @SerializedName("description") var  description: String = "",
        @SerializedName("normalPrice") var  normalPrice: Long = 0,
        @SerializedName("price") var  price: Long = 0,
        @SerializedName("pricePromo") var  pricePromo: Long = 0,
        @SerializedName("priceRipley") var  priceRipley: Long = 0,
        @SerializedName("line") var  line: String = "",
        @SerializedName("department") var  department: String = "",
        @SerializedName("discountPromo") var  discountPromo: Long = 0,
        @SerializedName("discountRipley") var  discountRipley: Long = 0,
        @SerializedName("codPromo") var  codPromo: ArrayList<String> = arrayListOf(),
        @SerializedName("codPromoRipley") var  codPromoRipley: ArrayList<String> = arrayListOf()
    )

}