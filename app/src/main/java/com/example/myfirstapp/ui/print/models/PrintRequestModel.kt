package com.example.myfirstapp.ui.print.models

import com.example.myfirstapp.ui.print.models.OrderTicketLineModel
import com.google.gson.annotations.SerializedName

data class PrintRequestModel (
    @SerializedName("mac") var Mac: String? = null,
    @SerializedName("ticket") var Ticket: List<OrderTicketLineModel>? = null
)