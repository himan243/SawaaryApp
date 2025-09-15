package com.adtu.sawaary.auth

import com.adtu.sawaary.data.model.User
import com.adtu.sawaary.data.model.UserType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    
    val currentUser = firebaseAuth.currentUser
    val isUserLoggedIn: Boolean
        get() = currentUser != null
    
    suspend fun signIn(email: String, password: String): Result<User?> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
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
    
    suspend fun signUp(
        email: String, 
        password: String, 
        name: String, 
        phoneNumber: String,
        userType: UserType
    ): Result<User?> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                val newUser = User(
                    uid = user.uid,
                    email = email,
                    name = name,
                    phoneNumber = phoneNumber,
                    userType = userType,
                    isOnline = true,
                    createdAt = System.currentTimeMillis()
                )
                saveUserData(newUser)
                Result.success(newUser)
            } else {
                Result.failure(Exception("Sign up failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signOut(): Result<Unit> {
        return try {
            currentUser?.let { user ->
                updateUserOnlineStatus(user.uid, false)
            }
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUserData(uid: String): User? {
        return try {
            val snapshot = database.reference.child("users").child(uid).get().await()
            snapshot.getValue(User::class.java)
        } catch (e: Exception) {
            null
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
    
    suspend fun updateUserProfile(user: User): Result<Unit> {
        return try {
            database.reference.child("users").child(user.uid).setValue(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
