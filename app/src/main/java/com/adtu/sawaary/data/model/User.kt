package com.adtu.sawaary.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val userType: UserType = UserType.TRAVELER,
    val isOnline: Boolean = false,
    val lastSeen: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)

enum class UserType {
    DRIVER,
    TRAVELER
}

