package com.example.myfirstapp.data.mapper

import com.example.myfirstapp.data.request.CheckPricesRequest
import com.example.myfirstapp.data.response.CheckPricesResponse

class DataMapper {

    fun reverseCheck(check: CheckPricesResponse) = CheckPricesRequest().apply {
        this.subsidiary = check.subsidiary
        this.session = check.session
        this.coupons = check.coupons
        this.products = reverseList(check.products)
    }

    private fun reverseList(input: List<CheckPricesResponse.Product>): ArrayList<CheckPricesRequest.Product> {
        val list = arrayListOf<CheckPricesRequest.Product>()

        for (item in input) {
            list.add(reverseMapZone(item))
        }

        return list
    }

    private fun reverseMapZone(item: CheckPricesResponse.Product) = CheckPricesRequest.Product().apply {
        this.quantity = item.quantity.toInt()
        this.sku = item.sku
        this.id = item.id.toInt()
    }
}