package dev.korryr.bongesha


import WishlistScreen
import android.annotation.SuppressLint
import android.app.PendingIntent
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.facebook.CallbackManager
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.screens.BongaAccSettings
import dev.korryr.bongesha.screens.BongaCategory
import dev.korryr.bongesha.screens.BongaForgotPassword
import dev.korryr.bongesha.screens.BongaHelp
import dev.korryr.bongesha.screens.BongaSignIn
import dev.korryr.bongesha.screens.BongaSignUp
import dev.korryr.bongesha.screens.BongaWelcome
import dev.korryr.bongesha.screens.CartScreen
import dev.korryr.bongesha.screens.ChatScreen
import dev.korryr.bongesha.screens.ItemDetailsScreen
import dev.korryr.bongesha.screens.NotificationScreen
import dev.korryr.bongesha.screens.OrdersScreen
import dev.korryr.bongesha.screens.UserProfile
import dev.korryr.bongesha.screens.category.screens.Beverages
import dev.korryr.bongesha.ui.theme.BongeshaTheme
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.viewmodels.AuthState
import dev.korryr.bongesha.viewmodels.AuthViewModel
import dev.korryr.bongesha.viewmodels.CategoryViewModel
import dev.korryr.bongesha.viewmodels.Product
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var callbackManager: CallbackManager
    private lateinit var auth: FirebaseAuth

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        //FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(application)
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()
            val context = LocalContext.current
            val currentSignInState = rememberUpdatedState(authViewModel.authState.value)



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
                                    result.data?.let { data ->
                                        val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
                                        account?.let {
                                            authViewModel.signInWithGoogle(it.idToken ?: "", navController)
                                        }
                                    }
                                }
                            }
                        }
                    )

                    //callbackManager = CallbackManager.Factory.create()
                    val isUserSignedIn by authViewModel.isSignedIn.collectAsState()
                    val startDestination = if (isUserSignedIn) Route.Home.Category else Route.Home.SignUp

                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable(Route.Home.SignUp) {
                            BongaSignUp(
                                navController = navController,
                                authViewModel = authViewModel,
                                onGoogleSignIn = {
                                    val googleSignInClient = GoogleSignIn.getClient(
                                    context,
                                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(getString(R.string.web_client_id))
                                        .requestEmail()
                                        .build()
                                )

                                    val signInIntent = googleSignInClient.signInIntent
                                    val pendingIntent = PendingIntent.getActivity(
                                        context, 0, signInIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                                    )

                                    launcher.launch(
                                        IntentSenderRequest.Builder(pendingIntent.intentSender).build()
                                    )
                                },
                                onFacebookSignInClick = {
                                    authViewModel.signInWithFacebook(navController)
                                },
                                onSignIn = { email, password ->
                                    authViewModel.signIn(email, password, navController)
                                }
                            )

                            LaunchedEffect(key1 = currentSignInState.value) {
                                if (currentSignInState.value is AuthState.Success) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        (currentSignInState.value as AuthState.Success).message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                    saveUserSignInState()
                                    navController.navigate(Route.Home.Category)
                                }
                            }
                        }

                        composable(Route.Home.SignIn) {
                            BongaSignIn(
                                navController = navController,
                                onForgotPassword = { email ->
                                    Firebase.auth.sendPasswordResetEmail(email)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(context, "Password reset email sent.", Toast.LENGTH_LONG).show()
                                            } else {
                                                Toast.makeText(context, "Error sending reset email.", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                },
                                onSignIn = { email, password ->
                                    authViewModel.signIn(email, password, navController)  // Handle email/password sign-in
                                },
                                authViewModel = authViewModel,
                                onGoogleSignIn = { idToken ->
                                    authViewModel.signInWithGoogle(idToken, navController) // Handle Google sign-in
                                    Toast.makeText(context, "Google sign-in failed: ID token is null.", Toast.LENGTH_LONG).show()
                                }
                            )
                        }


                        composable(Route.Home.Category) {
                            BongaCategory(
                                navController = navController,
                                categoryViewModel = CategoryViewModel()
                            )
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
                            CartScreen(navController = navController)
                        }

                        composable(Route.Home.ItemDetails) {
                            ItemDetailsScreen(
                                onClick = {},
                                product = Product()
                            )
                        }

                        composable(Route.Home.Inbox) {
                            ChatScreen()
                        }

                        composable(Route.Home.Notification) {
                            NotificationScreen()
                        }

                        composable(Route.Home.Profile) {
                            UserProfile(
                                navController = navController,
                            ) {
                                Firebase.auth.signOut()
                                clearUserSignInState()
                                navController.navigate(Route.Home.SignIn)
                            }
                        }

                        composable(Route.Home.HelpSupport) {
                            BongaHelp(navController = navController)
                        }

                        composable(Route.Home.AccountSettings) {
                            BongaAccSettings(navController = navController)
                        }

                        composable(Route.Home.Order) {
                            OrdersScreen()
                        }

                        composable(Route.Home.Wishlist) {
                            WishlistScreen(
                                product = Product(),
                            )
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

    fun saveUserDetails(email: String?, displayName: String?) {
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
