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
import dev.korryr.bongesha.viewmodels.AuthViewModel
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CancellationException

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient,
    private val authViewModel: AuthViewModel
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
            val email = credential.id

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
            user?.let {
                saveOrUpdateUserInFirestore(it)
            } // Save user data to Firestore if sign-in succeeds

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
        oneTapClient.signOut().await()
        authViewModel.deleteUserAccount { isDeleted ->
            if (isDeleted) {
                println("User account and data deleted successfully.")
            } else {
                println("Failed to delete user account and data.")
            }
        }
    }

    // Save or update the user in Firestore
    private suspend fun saveOrUpdateUserInFirestore(user: FirebaseUser) {
        val userName = user.displayName ?: "Anonymous-user"

        val userDocRef = firestore.collection("users").document(userName)

        val userData = mapOf(
            "userId" to user.uid,
            "email" to user.email,
            "displayName" to user.displayName,
            "profilePictureUrl" to user.photoUrl?.toString()
        )


        // Check if a document with this display name already exists
        val existingUserDoc = userDocRef.get().await()
        if (!existingUserDoc.exists()) {
            // Create new document if it doesn't exist
            userDocRef.set(userData)
                .addOnSuccessListener {
                    println("User data successfully saved to Firestore.")
                }
                .addOnFailureListener { e ->
                    println("Error saving user data to Firestore: ${e.message}")
                }
        } else {
            // Update the existing document if display name matches
            userDocRef.update(userData)
                .addOnSuccessListener {
                    println("User data successfully updated in Firestore.")
                }
                .addOnFailureListener { e ->
                    println("Error updating user data in Firestore: ${e.message}")
                }
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
