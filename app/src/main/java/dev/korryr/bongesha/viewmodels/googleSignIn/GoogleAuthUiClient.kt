package dev.korryr.bongesha.viewmodels.googleSignIn

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dev.korryr.bongesha.R
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CancellationException

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth
    private val firestore = FirebaseFirestore.getInstance()

    // Initiate Google Sign-In
    suspend fun signIn(): IntentSender? {
        return try {
            oneTapClient.beginSignIn(buildSignInRequest()).await()?.pendingIntent?.intentSender
        } catch (e: Exception) {
            Log.e("GoogleAuthUiClient", "Sign-in initiation failed: ${e.message}")
            null
        }
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        return try {
            val credential = oneTapClient.getSignInCredentialFromIntent(intent)
            val googleIdToken = credential.googleIdToken
            val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
            val email = credential.id ?: return SignInResult(
                data = null,
                errorMessage = "Google account does not have an associated email."
            )

            // Check for an existing email/password account with this email
            val existingSignInMethods = FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email).await()
            if (existingSignInMethods.signInMethods?.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD) == true) {
                return SignInResult(
                    data = null,
                    errorMessage = "This email is already registered with email/password. Please use that method to sign in."
                )
            }

            // Attempt Firebase sign-in with Google credentials
            val user = auth.signInWithCredential(googleCredentials).await().user
            user?.let { saveUserToFirestore(it) } // Save user data to Firestore if sign-in succeeds

            SignInResult(
                data = user?.toAppUserData(),
                errorMessage = null
            )
        } catch (e: Exception) {
            Log.e("GoogleAuthUiClient", "Sign-in failed: ${e.message}")
            SignInResult(
                data = null,
                errorMessage = e.message ?: "Sign-in failed due to an unknown error."
            )
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
            Log.d("GoogleAuthUiClient", "User signed out successfully")
        } catch (e: Exception) {
            Log.e("GoogleAuthUiClient", "Sign-out failed: ${e.message}")
            if (e is CancellationException) throw e
        }
    }

    // Save user details to Firestore under a document named with the display name
    private suspend fun saveUserToFirestore(user: FirebaseUser) {
        val userName = user.displayName ?: "Anonymous-user"
        val userData = mapOf(
            "userId" to user.uid,
            "displayName" to user.displayName,
            "email" to user.email,
            "profilePictureUrl" to user.photoUrl?.toString()
        )

        firestore.collection("users").document(userName).set(userData)
            .addOnSuccessListener {
                Log.d("GoogleAuthUiClient", "User data successfully saved to Firestore.")
            }
            .addOnFailureListener { e ->
                Log.e("GoogleAuthUiClient", "Error saving user data to Firestore: ${e.message}")
            }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}

// Extension function to convert FirebaseUser to AppUserData
private fun FirebaseUser.toAppUserData(): AppUserData {
    return AppUserData(
        userId = uid,
        username = displayName,
        profilePictureUrl = photoUrl?.toString()
    )
}

// Data classes
data class AppUserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)

data class SignInResult(
    val data: AppUserData?,
    val errorMessage: String?
)
