package com.adtu.sawaary.auth

import android.content.Context
import com.adtu.sawaary.data.model.User
import com.adtu.sawaary.data.model.UserType
import com.adtu.sawaary.firebase.FirebaseManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthHelper(private val context: Context) {
    
    private val auth: FirebaseAuth = FirebaseManager.getAuth()
    private val database = FirebaseManager.getDatabase()
    
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User?> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                val userData = getUserData(user.uid)
                Result.success(userData)
            } else {
                Result.failure(Exception("Sign in failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        name: String,
        phoneNumber: String,
        userType: UserType
    ): Result<User?> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                // Create user profile
                val newUser = User(
                    uid = user.uid,
                    email = email,
                    name = name,
                    phoneNumber = phoneNumber,
                    userType = userType,
                    isOnline = true,
                    createdAt = System.currentTimeMillis()
                )
                
                // Save user data to database
                saveUserData(newUser)
                
                // If user is a driver, create driver profile
                if (userType == UserType.DRIVER) {
                    createDriverProfile(user.uid)
                }
                
                Result.success(newUser)
            } else {
                Result.failure(Exception("User creation failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signOut(): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // Update user online status to false
                updateUserOnlineStatus(currentUser.uid, false)
            }
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUserData(uid: String): User? {
        return suspendCancellableCoroutine { continuation ->
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    continuation.resume(user)
                }
                
                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(Exception(error.message))
                }
            }
            
            database.reference.child("users").child(uid).addListenerForSingleValueEvent(listener)
            
            continuation.invokeOnCancellation {
                database.reference.child("users").child(uid).removeEventListener(listener)
            }
        }
    }
    
    private suspend fun saveUserData(user: User): Result<Unit> {
        return try {
            database.reference.child("users").child(user.uid).setValue(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private suspend fun createDriverProfile(uid: String): Result<Unit> {
        return try {
            val driverData = mapOf(
                "uid" to uid,
                "isVerified" to false,
                "isActive" to false,
                "rating" to 0.0,
                "totalTrips" to 0,
                "joinedDate" to System.currentTimeMillis()
            )
            
            database.reference.child("drivers").child(uid).setValue(driverData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private suspend fun updateUserOnlineStatus(uid: String, isOnline: Boolean): Result<Unit> {
        return try {
            val updates = mapOf(
                "isOnline" to isOnline,
                "lastSeen" to System.currentTimeMillis()
            )
            database.reference.child("users").child(uid).updateChildren(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getCurrentUser(): FirebaseUser? = auth.currentUser
    
    fun isUserLoggedIn(): Boolean = auth.currentUser != null
}

