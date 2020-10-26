package com.example.myfirstapp.data.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author @gerardoflores
 */
@Suppress("SpellCheckingInspection")
data class User(
    @SerializedName("ID") var id: Int = 0,
    @SerializedName("DOCUMENTNUMBER") var document_number: String = "",
    @SerializedName("FIRSTNAME") var first_name: String = "",
    @SerializedName("LASTNAME") var last_name: String = "",
    @SerializedName("PHONE") var phone: String = "",
    @SerializedName("EMAIL") var email: String = ""
) : Serializable