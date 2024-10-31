package dev.korryr.bongesha.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.putString
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dev.korryr.bongesha.commons.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var sharedPreferences: SharedPreferences
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> get() = _authState
    private val _isUserSignedIn = MutableStateFlow(Firebase.auth.currentUser != null)
    val isUserSignedIn: StateFlow<Boolean> = _isUserSignedIn

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val callbackManager = CallbackManager.Factory.create()
    private val context = getApplication<Application>().applicationContext
    private val _isSignedIn = MutableStateFlow(false)
    val isSignedIn: StateFlow<Boolean> get() = _isSignedIn

    init {
        checkSignInStatus()
    }

    private fun checkSignInStatus() {
        _isSignedIn.value = FirebaseAuth.getInstance().currentUser != null
    }

    fun signUp(email: String, password: String, displayName: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build()

                    user?.updateProfile(profileUpdates)?.addOnCompleteListener { profileUpdateTask ->
                        if (profileUpdateTask.isSuccessful) {

                            // Send the email verification
                            user.sendEmailVerification().addOnCompleteListener { verificationTask ->
                                if (verificationTask.isSuccessful) {
                                    //saveUserToFirestore(user)
                                    _authState.value = AuthState.Success("Verification email sent to your email address.")
                                } else {
                                    _authState.value = AuthState.Error(
                                        verificationTask.exception?.message ?: "Failed to send verification email"
                                    )
                                }
                            }
                        } else {
                            _authState.value = AuthState.Error(profileUpdateTask.exception?.message ?: "Profile update failed")
                        }
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Sign-up failed")
                }
            }
        }
    }

    fun signIn(email: String, password: String, navController: NavController) {

        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password cannot be empty")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user?.isEmailVerified == true) {
                        saveUserToFirestore(user)
                        _authState.value = AuthState.Success("Sign in successful")
                        navController.navigate(Route.Home.HOME)
                    } else {
                        _authState.value = AuthState.Error("Please verify your email first")
                        auth.signOut()
                    }
                } else {
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidCredentialsException -> "Incorrect email or password."
                        is FirebaseAuthInvalidUserException -> "User not found. Please sign up first."
                        else -> task.exception?.message ?: "Sign in failed. Please try again."
                    }
                    _authState.value = AuthState.Error(errorMessage)
                }
            }
        }
    }


//    fun signInWithEmail(email: String, password: String, navController: NavController, context: Context) {
//        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    // Navigate to home after successful login
//                    navController.navigate(Route.Home.Category)
//                } else {
//                    Toast.makeText(context, "Sign-in failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
//                }
//            }
//    }


    fun signInWithGoogle(idToken: String, navController: NavController) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveUserToFirestore(auth.currentUser)
                    _authState.value = AuthState.Success("Google Sign in successful")
                    navController.navigate(Route.Home.HOME)
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Google Sign in failed")
                }
            }
        }
    }

    fun signInWithFacebook(navController: NavController) {
        val loginButton = LoginButton(context) // Dynamically creating a LoginButton
        loginButton.setPermissions("email", "public_profile")
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                auth.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveUserToFirestore(auth.currentUser)
                        _authState.value = AuthState.Success("Facebook sign-in successful")
                        navController.navigate(Route.Home.HOME)
                    } else {
                        _authState.value = AuthState.Error(task.exception?.message ?: "Facebook sign-in failed")
                    }
                }
            }

            override fun onCancel() {
                _authState.value = AuthState.Error("Facebook sign-in canceled")
            }

            override fun onError(error: FacebookException) {
                _authState.value = AuthState.Error("Error: ${error.message}")
            }
        })
    }


    private fun saveUserToFirestore(user: FirebaseUser?) {
        user?.let {
            val userName = it.displayName ?: "Anonymous-user"
            //val userDocument = firestore.collection(userName).document("details")

            val userData = mapOf(
                "uid" to it.uid,
                "email" to it.email,
                "displayName" to it.displayName
            )

            firestore.collection("users").document(userName).set(userData)
                .addOnSuccessListener {
                    Log.d("Firestore", "Account created successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error processing userData", e)
                }
        }
    }

    fun signOut() {
        Firebase.auth.signOut()
        _isUserSignedIn.value = false
    }






}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

