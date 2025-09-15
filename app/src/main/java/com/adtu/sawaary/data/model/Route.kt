package com.adtu.sawaary.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Route(
    val routeId: String = "",
    val routeName: String = "",
    val startLocation: Location = Location(),
    val endLocation: Location = Location(),
    val waypoints: List<Location> = emptyList(),
    val estimatedDuration: Long = 0L, // in minutes
    val distance: Double = 0.0, // in kilometers
    val isActive: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@IgnoreExtraProperties
data class Trip(
    val tripId: String = "",
    val driverId: String = "",
    val busId: String = "",
    val route: Route = Route(),
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null,
    val status: TripStatus = TripStatus.PENDING,
    val currentLocation: Location = Location(),
    val passengersCount: Int = 0,
    val estimatedArrival: Long = 0L
)

enum class TripStatus {
    PENDING,
    IN_PROGRESS,
    PAUSED,
    COMPLETED,
    CANCELLED
}

