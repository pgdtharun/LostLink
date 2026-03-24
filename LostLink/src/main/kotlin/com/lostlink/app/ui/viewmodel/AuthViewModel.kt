package com.lostlink.app.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.lostlink.app.data.model.User
import com.lostlink.app.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date

class AuthViewModel : ViewModel() {
    private val auth = Firebase.auth

    private val _authState = MutableStateFlow<AuthState>(AuthState.LoggedOut)
    val authState = _authState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _authState.value = AuthState.LoggedIn(currentUser.uid)
        }
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit) {
        Log.d("AuthViewModel", "Signing in with $email")
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                Log.d("AuthViewModel", "Sign in successful: ${result.user?.uid}")
                _authState.value = AuthState.LoggedIn(result.user!!.uid)
                _isLoading.value = false
                onSuccess()
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Sign in failed", e)
                _error.value = e.message ?: "Authentication failed"
                _isLoading.value = false
            }
        }
    }

    fun signInAsAdmin(onSuccess: () -> Unit) {
        Log.d("AuthViewModel", "Bypassing authentication as Admin")
        _authState.value = AuthState.LoggedInWithUid("admin_bypass_uid")
        onSuccess()
    }

    fun signUp(name: String, email: String, phone: String, idNumber: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val user = User(
                    id = result.user!!.uid,
                    name = name,
                    email = email,
                    phone = phone,
                    avatarUrl = null,
                    idNumber = idNumber,
                    joinDate = Date(),
                    isVerified = false
                )
                FirebaseRepository.saveUserProfile(user)
                _authState.value = AuthState.LoggedIn(result.user!!.uid)
                _isLoading.value = false
                onSuccess()
            } catch (e: Exception) {
                _error.value = e.message ?: "Registration failed"
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.LoggedOut
    }
}

sealed class AuthState {
    object LoggedIn : AuthState() {
        // Simple variant for check
    }
    data class LoggedInWithUid(val uid: String) : AuthState()
    object LoggedOut : AuthState()
    
    // helper to avoid data class vs object issues in simple UI checks
    companion object {
        fun LoggedIn(uid: String) = LoggedInWithUid(uid)
    }
}
