package com.adtu.sawaary.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val accuracy: Float = 0f,
    val speed: Float = 0f,
    val bearing: Float = 0f,
    val timestamp: Long = System.currentTimeMillis(),
    val address: String = ""
)

data class LocationUpdate(
    val busId: String = "",
    val location: Location = Location(),
    val isActive: Boolean = false
)

