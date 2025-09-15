package com.adtu.sawaary.firebase

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

object FirebaseManager {
    
    fun initialize(context: Context) {
        // Firebase is automatically initialized when google-services.json is present
        // But we can ensure it's properly set up
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
    }
    
    fun getAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    
    fun getDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()
    
    fun getMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()
    
    // Database reference helpers
    fun getUsersRef() = getDatabase().reference.child("users")
    fun getDriversRef() = getDatabase().reference.child("drivers")
    fun getBusLocationsRef() = getDatabase().reference.child("bus_locations")
    fun getTripsRef() = getDatabase().reference.child("trips")
    fun getTripHistoryRef() = getDatabase().reference.child("trip_history")
    fun getFcmTokensRef() = getDatabase().reference.child("fcm_tokens")
}

