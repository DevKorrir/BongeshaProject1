package dev.korryr.bongesha.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dev.korryr.bongesha.commons.presentation.sign_in.GoogleAuthUiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val googleAuthUiClient: GoogleAuthUiClient
) : ViewModel() {

    private val _authState = MutableStateFlow<GoogleSignInAccount?>(null)
    val authState: StateFlow<GoogleSignInAccount?> get() = _authState

    init {
        viewModelScope.launch {
           // _authState.value = googleAuthUiClient.getSignedInAccount()
        }
    }

    fun signIn() {
        viewModelScope.launch {
            val signInIntentSender = googleAuthUiClient.signIn()
            // Handle the sign-in intent sender here, such as launching it in your activity/fragment
        }
    }

    fun signOut() {
        viewModelScope.launch {
            googleAuthUiClient.signOut()
            _authState.value = null
        }
    }
}
