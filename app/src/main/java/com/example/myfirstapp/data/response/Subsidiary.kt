package com.example.myfirstapp.data.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author @gerardoflores
 */
data class Subsidiary(
    @SerializedName("ID") var id: Int = 0,
    @SerializedName("DESCRIPTION") var description: String = "",
    @SerializedName("CODE") var code: String = "",
    @SerializedName("COD8E_PROM") var code8e_prom: Int = 0,
    @SerializedName("LATITUDE") var latitude: Double = 0.0,
    @SerializedName("LONGITUDE") var longitude: Double = 0.0
) : Serializable