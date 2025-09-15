package com.adtu.sawaary.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Bus(
    val busId: String = "",
    val driverId: String = "",
    val licensePlate: String = "",
    val vehicleNumber: String = "",
    val capacity: Int = 50,
    val currentLocation: Location? = null,
    val currentRoute: Route? = null,
    val isActive: Boolean = false,
    val lastUpdate: Long = System.currentTimeMillis(),
    val status: BusStatus = BusStatus.OFFLINE
)

enum class BusStatus {
    ONLINE,
    OFFLINE,
    ON_BREAK,
    MAINTENANCE
}

