package dev.korryr.bongesha.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.identity.Identity
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
import dev.korryr.bongesha.screens.isValidEmail
import dev.korryr.bongesha.screens.isValidPassword
import dev.korryr.bongesha.viewmodels.googleSignIn.GoogleAuthUiClient
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
    companion object {
        private const val PREFS_KEY_SIGNED_IN = "isUserSignedIn"
    }

    init {
        sharedPreferences = initEncryptedSharedPreferences()
        checkSignInStatus()
    }

    private val googleAuthUiClient: GoogleAuthUiClient = GoogleAuthUiClient(
        context = getApplication(),
        oneTapClient = Identity.getSignInClient(getApplication())
    )

    fun startGoogleSignIn(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        viewModelScope.launch {
            val intentSender = googleAuthUiClient.signIn()
            intentSender?.let {
                val request = IntentSenderRequest.Builder(it).build()
                launcher.launch(request)
            }
        }
    }

    fun handleGoogleSignInResult(data: Intent) {
        viewModelScope.launch {
            val result = googleAuthUiClient.signInWithIntent(data)
            _authState.value = if (result.data != null) {
                AuthState.Success("Google Sign-In successful")
            } else {
                AuthState.Error(result.errorMessage ?: "Google Sign-In failed")
            }
        }
    }

    private fun initEncryptedSharedPreferences(): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            "user_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun checkSignInStatus() {
        _isUserSignedIn.value = FirebaseAuth.getInstance().currentUser != null
    }

    fun signUp(
        email: String,
        password: String,
        displayName: String
    ) {
        if (!isValidEmail(email) || !isValidPassword(password)) {
            _authState.value = AuthState.Error("Invalid email or password format")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
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
                                    _authState.value = AuthState.SignUpSuccess//("Verification email sent to your email address.")
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

    fun signIn(
        email: String,
        password: String,
        navController: NavController
    ) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password cannot be empty")
            return
        }
        if (!isValidEmail(email) || !isValidPassword(password)) {
            _authState.value = AuthState.Error("Invalid email or password format")
            return
        }

        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val signInMethods = task.result?.signInMethods ?: emptyList()
                if (signInMethods.contains(GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD)) {
                    _authState.value =
                        AuthState.Error("This email is already registered with Google. Please use Google to sign in.")
                    return@addOnCompleteListener
                }

                viewModelScope.launch {
                    _authState.value = AuthState.Loading
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            if (user?.isEmailVerified == true) {
                                saveUserToFirestore(user)
                                _authState.value = AuthState.Success("Sign in successful")
                            } else {
                                _authState.value = AuthState.Error("Please verify your email first")
                                auth.signOut()
                            }
                        } else {
                            val errorMessage = when (task.exception) {
                                is FirebaseAuthInvalidCredentialsException -> "Incorrect email or password."
                                is FirebaseAuthInvalidUserException -> "User not found. Please sign up first."
                                else -> task.exception?.message
                                    ?: "Sign in failed. Please try again."
                            }
                            _authState.value = AuthState.Error(errorMessage)
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun signInWithGoogle(
        idToken: String?,
        navController: NavController
    ) {
        if (idToken == null) {
            _authState.value = AuthState.Error("Google sign-in failed: Missing token")
            return
        }

        val credential = GoogleAuthProvider.getCredential(idToken, null)

                    Firebase.auth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = Firebase.auth.currentUser
                                user?.let {
                                    saveUserToFirestore(it)
                                    saveSignInState(true)
                                    _authState.value =
                                        AuthState.Success("Google sign-in successful")
                                    navController.navigate(Route.Home.HOME)
                                }
                            } else {
                                _authState.value =
                                    AuthState.Error("Google sign-in failed: ${task.exception?.message}")
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
                        saveSignInState(true)
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

    private fun saveSignInState(isUserSignedIn: Boolean) {
        sharedPreferences.edit().putBoolean(PREFS_KEY_SIGNED_IN, isUserSignedIn).apply()
    }


    fun signOut(navController: NavController) {
        Firebase.auth.signOut() // Sign out from Firebase
        _authState.value = AuthState.Idle // Reset the auth state or set it to Idle

        // Optionally, clear shared preferences or other user-related data
        clearUserSignInState()

        // Redirect to the sign-in screen
        navController.navigate(Route.Home.SIGN_IN) {
            popUpTo(Route.Home.HOME) { inclusive = true } // Clear the back stack
        }
    }

    private fun clearUserSignInState() {
        sharedPreferences.edit().apply {
            putBoolean("isUserSignedIn", false)
            remove("userEmail")
            remove("userDisplayName")
            apply()
        }
    }






}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object SignUpSuccess : AuthState() // New state for successful sign-up
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}


