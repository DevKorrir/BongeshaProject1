package dev.korryr.bongesha

import WishlistScreen
import android.annotation.SuppressLint
import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.facebook.appevents.AppEventsLogger
import com.google.android.libraries.places.api.Places
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.screens.AllProductsScreen
import dev.korryr.bongesha.screens.BongaAccSettings
import dev.korryr.bongesha.screens.BongaForgotPassword
import dev.korryr.bongesha.screens.BongaHelp
import dev.korryr.bongesha.screens.BongaHome
import dev.korryr.bongesha.screens.BongaSignIn
import dev.korryr.bongesha.screens.BongaSignUp
import dev.korryr.bongesha.screens.BongaWelcome
import dev.korryr.bongesha.screens.CartScreen
import dev.korryr.bongesha.screens.ChatScreen
import dev.korryr.bongesha.screens.CheckOut
import dev.korryr.bongesha.screens.DeleteAccountRequestScreen
import dev.korryr.bongesha.screens.GeneralSettingsScreen
import dev.korryr.bongesha.screens.ItemDetailsScreen
import dev.korryr.bongesha.screens.NotificationScreen
import dev.korryr.bongesha.screens.OrdersScreen
import dev.korryr.bongesha.screens.SplashScreen
import dev.korryr.bongesha.screens.SummaryScreen
import dev.korryr.bongesha.screens.ThankYouScreen
import dev.korryr.bongesha.screens.UserProfile
import dev.korryr.bongesha.ui.theme.BongeshaTheme
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.viewmodels.AuthState
import dev.korryr.bongesha.viewmodels.AuthViewModel
import dev.korryr.bongesha.viewmodels.CartItem
import dev.korryr.bongesha.viewmodels.CartViewModel
import dev.korryr.bongesha.viewmodels.CategoryViewModel
import dev.korryr.bongesha.viewmodels.Product
import dev.korryr.bongesha.viewmodels.SplashViewModel

class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        // Initialize the Places API with your API key
        if (!Places.isInitialized()) {
            Places.initialize(this, "AIzaSyDQ7ZxsA21JywS0UorNAW16ZS0Nfrz-eRo")
        }
        AppEventsLogger.activateApp(application)
        sharedPreferences = initEncryptedSharedPreferences()

        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()
            val context = LocalContext.current

            val splashViewModel: SplashViewModel = viewModel()
            val isLoading by splashViewModel.isLoading.collectAsState()

            val currentSignInState = rememberUpdatedState(authViewModel.authState.value)
            val isUserSignedIn by authViewModel.isUserSignedIn.collectAsState()

            // Result launcher for Google Sign-In
            val googleSignInLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.let { data ->
                        authViewModel.handleGoogleSignInResult(data)
                    }
                } else {
                    Toast.makeText(context, "Google sign-in canceled", Toast.LENGTH_LONG).show()
                }
            }

            // Observe auth state
            val authState by authViewModel.authState.collectAsState()
            LaunchedEffect(authState) {
                when (authState) {

                    is AuthState.Success -> {
                        navController.navigate(Route.Home.HOME) {
                            popUpTo(Route.Home.SIGN_IN) { inclusive = true }
                        }
                    }
                    is AuthState.Error -> {
                        Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_LONG).show()
                    }
                    else -> {}
                }
            }

            BongeshaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = gray01
                ) {
                    if (isLoading) {
                        SplashScreen() // Show splash screen while loading
                    } else {
                        //callbackManager = CallbackManager.Factory.create()
                        val isUserSignedIn by authViewModel.isUserSignedIn.collectAsState()
                        val startDestination =
                            if (isUserSignedIn) Route.Home.HOME else Route.Home.SIGN_UP

                        NavHost(
                            navController = navController,
                            startDestination = startDestination
                        ) {
                            composable(Route.Home.SIGN_UP) {
                                BongaSignUp(
                                    navController = navController,
                                    authViewModel = authViewModel,
                                    onGoogleSignIn = {
                                        // Trigger Google Sign-In through AuthViewModel
                                        authViewModel.startGoogleSignIn(googleSignInLauncher)
                                    },
                                    onFacebookSignInClick = {
                                        authViewModel.signInWithFacebook(navController)
                                    },
                                    onSignIn = { email, password ->
                                        authViewModel.signIn(email, password, navController)
                                    }
                                )
                            }

                            composable(Route.Home.SIGN_IN) {
                                BongaSignIn(
                                    navController = navController,
                                    onForgotPassword = { email ->
                                        Firebase.auth.sendPasswordResetEmail(email)
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
                                                        task.exception?.localizedMessage
                                                            ?: "Error sending reset email.",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            }
                                    },
                                    onSignIn = { email, password ->
                                        authViewModel.signIn(
                                            email,
                                            password,
                                            navController
                                        )  // Handle email/password sign-in
                                    },
                                    authViewModel = authViewModel,
                                    onGoogleSignIn = {
                                        authViewModel.startGoogleSignIn(googleSignInLauncher)
                                    }
                                )
                            }


                            composable(Route.Home.HOME) {
                                BongaHome(
                                    categoryViewModel = CategoryViewModel(),
                                    navController = navController
                                )
                            }

                            composable(Route.Home.FORGOT_PASSWORD) {
                                BongaForgotPassword(navController)
                            }

                            composable(Route.Home.WELCOME) {
                                BongaWelcome(navController = navController)
                            }


                            composable(Route.Home.CART) {
                                CartScreen(
                                    navController = navController,
                                    authViewModel = authViewModel,
                                    cartViewModel = CartViewModel(),
                                )
                            }

                            composable(Route.Home.ITEM_DETAILS) {
                                ItemDetailsScreen(
                                    onClick = {},
                                    product = Product(),
                                    cartItem = CartItem()
                                )
                            }

                            composable(Route.Home.INBOX) {
                                ChatScreen()
                            }

                            composable(Route.Home.NOTIFICATION) {
                                NotificationScreen()
                            }

                            composable(Route.Home.CATEGORY) {
                                AllProductsScreen(
                                    navController
                                )
                            }

                            composable(Route.Home.PROFILE) {
                                UserProfile(
                                    navController = navController,
                                    authViewModel = authViewModel

                                )
                            }

                            composable(Route.Home.HELP_SUPPORT) {
                                BongaHelp(navController = navController)
                            }

                            composable(Route.Home.ACCOUNT_SETTINGS) {
                                BongaAccSettings(navController = navController)
                            }

                            composable(Route.Home.ORDER_HISTORY) {
                                OrdersScreen(
                                    navController
                                )
                            }

                            composable(Route.Home.WISHLIST) {
                                WishlistScreen(
                                    product = Product(),
                                )
                            }

                            composable(Route.Home.CHECKOUT) {
                                CheckOut(
                                    navController,
                                )
                            }

                            composable(Route.Home.SETTINGS) {
                                GeneralSettingsScreen(
                                    authViewModel,
                                    navController
                                )
                            }

                            composable(Route.Home.DELETE_ACCOUNT) {
                                DeleteAccountRequestScreen(
                                    authViewModel,
                                    navController
                                )
                            }

                            composable(Route.Home.SUMMARY) {
                                SummaryScreen(
                                    cartViewModel = CartViewModel(),
                                    navController
                                )
                            }

                            composable(Route.Home.THANK_YOU) {
                                ThankYouScreen(
                                    navController
                                )
                            }





                        }
                    }
                }
            }
        }
    }

    private fun initEncryptedSharedPreferences(): SharedPreferences {
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            this,
            "user_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }




}
