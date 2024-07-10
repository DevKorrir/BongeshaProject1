package dev.korryr.bongesha.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import dev.korryr.bongesha.commons.Route
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
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // send verification email
                            val user = auth.currentUser
                            user?.sendEmailVerification()
                                ?.addOnCompleteListener { verificationTask ->
                                    if (verificationTask.isSuccessful) {
                                        _authState.value =
                                            AuthState.Success("Verification email sent")
                                    } else {
                                        _authState.value = AuthState.Error(
                                            verificationTask.exception?.message
                                                ?: "Verification email failed"
                                        )
                                    }
                                }
                            //_authState.value = AuthState.Success("Sign up successful")
                        } else {
                            _authState.value =
                                AuthState.Error(task.exception?.message ?: "Sign up failed")
                        }
                    }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign up failed")
            }
        }
    }


    fun signIn(
        email: String,
        password: String,
        navController: NavController
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            if (user != null && user.isEmailVerified) {
                                //saveUserDetails(user.email, user.displayName)
                                // Email is verified, proceed with sign-in
                                //saveUserSignInState()
                                _authState.value = AuthState.Success("Sign in successful")
                                navController.navigate(Route.Home.Category)
                            } else {
                                // Email is not verified, show an error message
                                _authState.value = AuthState.Error("Please verify your email first")
                                auth.signOut()
                            }

                            //_authState.value = AuthState.Success("Sign in successful")
                        } else {
                            // Handle sign-in failure
                            _authState.value = AuthState.Error(
                                task.exception?.message ?: "Enter correct credentials"
                            )
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

