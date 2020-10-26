package com.example.myfirstapp.data.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author @gerardoflores
 */
data class Parameter(
    @SerializedName("ID") var id: Int = 0,
    @SerializedName("CODE") var code: String = "",
    @SerializedName("DESCRIPTION") var description: String = "",
    @SerializedName("VALUE") var value: String = "",
    @Suppress("SpellCheckingInspection") @SerializedName("TYPEVALUE") var type_value: Int = 0,
    @Suppress("SpellCheckingInspection") @SerializedName("LENGTHVALUE") var length_value: Int = 0,
    @Suppress("SpellCheckingInspection") @SerializedName("CODETABLE") var code_table: Int = 0
) : Serializable