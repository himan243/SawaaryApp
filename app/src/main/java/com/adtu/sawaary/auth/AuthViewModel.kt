package com.adtu.sawaary.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adtu.sawaary.data.model.User
import com.adtu.sawaary.data.model.UserType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    init {
        checkAuthState()
    }
    
    private fun checkAuthState() {
        if (authRepository.isUserLoggedIn) {
            viewModelScope.launch {
                authRepository.currentUser?.let { firebaseUser ->
                    val user = authRepository.getUserData(firebaseUser.uid)
                    _currentUser.value = user
                    _authState.value = if (user != null) {
                        AuthState.Authenticated(user)
                    } else {
                        AuthState.Unauthenticated
                    }
                }
            }
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }
    
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.signIn(email, password)
                .onSuccess { user ->
                    _currentUser.value = user
                    _authState.value = if (user != null) {
                        AuthState.Authenticated(user)
                    } else {
                        AuthState.Unauthenticated
                    }
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Sign in failed")
                }
        }
    }
    
    fun signUp(
        email: String,
        password: String,
        name: String,
        phoneNumber: String,
        userType: UserType
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.signUp(email, password, name, phoneNumber, userType)
                .onSuccess { user ->
                    _currentUser.value = user
                    _authState.value = if (user != null) {
                        AuthState.Authenticated(user)
                    } else {
                        AuthState.Unauthenticated
                    }
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Sign up failed")
                }
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
                .onSuccess {
                    _currentUser.value = null
                    _authState.value = AuthState.Unauthenticated
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Sign out failed")
                }
        }
    }
    
    fun resetPassword(email: String) {
        viewModelScope.launch {
            authRepository.resetPassword(email)
                .onSuccess {
                    _authState.value = AuthState.PasswordResetSent
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Password reset failed")
                }
        }
    }
    
    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Unauthenticated
        }
    }
}

sealed class AuthState {
    object Loading : AuthState()
    object Unauthenticated : AuthState()
    data class Authenticated(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
    object PasswordResetSent : AuthState()
}
