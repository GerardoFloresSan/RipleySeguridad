package ripley.integracion.visanet.models

import com.google.gson.annotations.SerializedName

data class PrintRequestModel (
    @SerializedName("mac") var Mac: String? = null,
    @SerializedName("ticket") var Ticket: List<OrderTicketLineModel>? = null
)