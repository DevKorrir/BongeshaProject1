package dev.korryr.bongesha

import BongaSignUp
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.screens.UserProfile
import dev.korryr.bongesha.commons.presentation.sign_in.GoogleAuthUiClient
import dev.korryr.bongesha.commons.presentation.sign_in.SignInViewModel
import dev.korryr.bongesha.screens.*
import dev.korryr.bongesha.screens.category.BongaCategory
import dev.korryr.bongesha.screens.category.screens.Beverages
import dev.korryr.bongesha.ui.theme.BongeshaTheme
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.ui.theme.orange01
import dev.korryr.bongesha.viewmodels.AuthViewModelMail
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        setContent {
            val navController = rememberNavController()
            val viewModel = viewModel<SignInViewModel>()
            val context = LocalContext.current
            val currentSignInState = rememberUpdatedState(viewModel.state.value.isSignInSuccessful)

            window?.statusBarColor = Color.Black.toArgb()

            BongeshaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = gray01
                ) {
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = { result ->
                            if (result.resultCode == RESULT_OK) {
                                lifecycleScope.launch {
                                    val signInResult = googleAuthUiClient.signInWithIntent(
                                        intent = result.data ?: return@launch
                                    )
                                    viewModel.onSignInResult(signInResult)
                                }
                            }
                        }
                    )

                    NavHost(
                        navController = navController,
                        startDestination = if (isUserSignedIn()) Route.Home.Category else Route.Home.SignUp
                    ) {
                        composable(Route.Home.SignUp) {
                            BongaSignUp(
                                navController = navController,
                                authViewModel = AuthViewModelMail()
                            ) {
                                lifecycleScope.launch {
                                    val signInIntentSender = googleAuthUiClient.signIn()
                                    launcher.launch(
                                        IntentSenderRequest.Builder(
                                            signInIntentSender ?: return@launch
                                        ).build()
                                    )
                                }
                            }
                            LaunchedEffect(key1 = currentSignInState.value) {
                                if (currentSignInState.value) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Account Created successful",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    saveUserSignInState()
                                    navController.navigate(Route.Home.Category)
                                    viewModel.resetState()
                                }
                            }
                        }

                        composable(Route.Home.SignIn) {
                            BongaSignIn(
                                navController = navController,
                                onClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                },
                                onForgotPassword = { email ->
                                    auth.sendPasswordResetEmail(email)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(
                                                    context,
                                                    "Password reset email sent.",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Error sending reset email.",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                },
                                onSignIn = { email, password ->
                                    auth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                val user = auth.currentUser
                                                if (user != null && user.isEmailVerified) {
                                                    saveUserDetails(user.email, user.displayName)
                                                    saveUserSignInState()
                                                    Toast.makeText(
                                                        context,
                                                        "Sign in successful",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                    navController.navigate(Route.Home.Category)
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Please verify your email first",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                    auth.signOut()
                                                }
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Sign in failed",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                }
                            )
                        }

                        composable(Route.Home.Category) {
                            BongaCategory(navController = navController) {}
                        }

                        composable(Route.Home.ForgotPassword) {
                            BongaForgotPassword(navController)
                        }

                        composable(Route.Home.Welcome) {
                            BongaWelcome(navController = navController)
                        }

                        composable(Route.Home.Beverage) {
                            Beverages()
                        }

                        composable(Route.Home.Cart) {
                            CartScreen(
                                navController = navController,
                                cartItems = emptyList(),
                            )
                        }

                        composable(Route.Home.ItemDetails) {
                            ItemDetailsScreen(
                                itemId = it.arguments?.getString("itemId") ?: "",
                            )
                        }

                        composable(Route.Home.Inbox) {
                            ChatScreen()
                        }

                        composable(Route.Home.Notification) {
                            NotificationScreen()
                        }

                        composable(Route.Home.Profile) {
                            UserProfile(navController = navController) {
                                auth.signOut()
                                clearUserSignInState()
                                navController.navigate(Route.Home.SignIn)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isUserSignedIn(): Boolean {
        return sharedPreferences.getBoolean("isSignedIn", false)
    }

    private fun saveUserSignInState() {
        sharedPreferences.edit().putBoolean("isSignedIn", true).apply()
    }

    private fun saveUserDetails(email: String?, displayName: String?) {
        sharedPreferences.edit().apply {
            putString("userEmail", email)
            putString("userDisplayName", displayName)
            apply()
        }
    }

    private fun clearUserSignInState() {
        sharedPreferences.edit().apply {
            putBoolean("isSignedIn", false)
            remove("userEmail")
            remove("userDisplayName")
            apply()
        }
    }
}
