package com.example.myfirstapp.data.response

import java.io.Serializable

data class LocationUser (
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) : Serializable