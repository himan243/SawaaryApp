package com.adtu.sawaary.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class BusStop(
    val stopId: String = "",
    val name: String = "",
    val location: Location = Location(),
    val routes: List<String> = emptyList(), // Route IDs that pass through this stop
    val isActive: Boolean = true,
    val facilities: List<String> = emptyList() // e.g., "shelter", "bench", "lighting"
)

@IgnoreExtraProperties
data class BusArrival(
    val busId: String = "",
    val stopId: String = "",
    val estimatedArrival: Long = 0L, // timestamp
    val distance: Double = 0.0, // in kilometers
    val isDelayed: Boolean = false,
    val delayMinutes: Int = 0
)

