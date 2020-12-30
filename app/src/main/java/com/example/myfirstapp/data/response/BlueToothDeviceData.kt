package com.example.myfirstapp.data.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BlueToothDeviceData (
    @SerializedName("name") var name : String = "",
    @SerializedName("address_mac") var addressMac : String = ""
) : Serializable