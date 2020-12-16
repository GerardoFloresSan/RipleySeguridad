package ripley.integracion.visanet.models

import com.google.gson.annotations.SerializedName

public class OrderTicketLineModel() {

    constructor(
            text: String? = null,        align: String? = null,         tipo: String? = null,
            values: String? = null,      text1: String? = null,         text2: String? = null,
            sizeColumn1: String? = null, sizeColumn3: String? = null,   textLeft: String? = null,
            textRight: String? = null,   bold: String? = null
        ) : this() {
        this.text = text
        this.align = align
        this.tipo = tipo
        this.values = values
        this.text1 = text1
        this.text2 = text2
        this.sizeColumn1 = sizeColumn1
        this.sizeColumn3 = sizeColumn3
        this.textLeft = textLeft
        this.textRight = textRight
        this.bold = bold
    }

    @SerializedName("text") var text: String? = null

    @SerializedName("align") var align: String? =  "LEFT";

    @SerializedName("tipo") var tipo: String? = null

    @SerializedName("value") var values: String? = null

    @SerializedName("text1") var text1: String? = null

    @SerializedName("text2") var text2: String? = null

    @SerializedName("sizeColumn1") var sizeColumn1: String? = null

    @SerializedName("sizeColumn3") var sizeColumn3: String? = null

    @SerializedName("textLeft") var textLeft: String? = null

    @SerializedName("textRight") var textRight: String? = null

    @SerializedName("bold") var bold: String? = "0";
}