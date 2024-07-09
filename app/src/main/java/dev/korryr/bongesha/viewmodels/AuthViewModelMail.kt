package dev.korryr.bongesha.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModelMail : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> get() = _authState

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authState.value = AuthState.Success("Sign up successful")
                    } else {
                        _authState.value = AuthState.Error(task.exception?.message ?: "Sign up failed")
                    }
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign up failed")
            }
        }
    }


fun signIn(email: String, password: String) {
    viewModelScope.launch {
        _authState.value = AuthState.Loading
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success("Sign in successful")
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Enter correct credentials")
                }
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "Sign inn failed")
        }
    }
}
}

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

