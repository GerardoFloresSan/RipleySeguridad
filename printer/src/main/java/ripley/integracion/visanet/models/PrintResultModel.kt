package ripley.integracion.visanet.models

import com.google.gson.annotations.SerializedName

data class  PrintResultModel (
    @SerializedName("estado") var Estado: Int = 0,
    @SerializedName("mensaje") var Mensaje: String? = ""
)