package com.adtu.sawaary.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Driver(
    val uid: String = "",
    val licensePlate: String = "",
    val vehicleNumber: String = "",
    val driverLicense: String = "",
    val isVerified: Boolean = false,
    val isActive: Boolean = false,
    val currentTrip: Trip? = null,
    val rating: Double = 0.0,
    val totalTrips: Int = 0,
    val joinedDate: Long = System.currentTimeMillis()
)

