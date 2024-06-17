package dev.korryr.bongesha

import android.annotation.SuppressLint
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
import dev.korryr.bongesha.commons.presentation.sign_in.GoogleAuthUiClient
import dev.korryr.bongesha.commons.presentation.sign_in.SignInViewModel
import dev.korryr.bongesha.screens.BongaSignIn
import dev.korryr.bongesha.screens.BongaSignUp
import dev.korryr.bongesha.screens.BongaWelcome
import dev.korryr.bongesha.screens.CartScreen
import dev.korryr.bongesha.screens.ItemDetailsScreen
import dev.korryr.bongesha.screens.category.BongaCategory
//import dev.korryr.bongesha.screens.category.BongaCategory
import dev.korryr.bongesha.screens.category.screens.Beverages
import dev.korryr.bongesha.ui.theme.BongeshaTheme
import dev.korryr.bongesha.ui.theme.gray01
import dev.korryr.bongesha.viewmodels.CartItemViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    private lateinit var auth: FirebaseAuth

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        setContent {

            val navController = rememberNavController()
            val viewModel = viewModel<SignInViewModel>()
            val context = LocalContext.current
            val currentSignInState = rememberUpdatedState(viewModel.state.value.isSignInSuccessful)

            window?.statusBarColor = Color.Black.toArgb()

            BongeshaTheme {
                // A surface container using the 'background' color from the theme
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
                        startDestination = Route.Home.SignUp
                    ) {
                        composable(Route.Home.SignUp) {
                            BongaSignUp(navController = navController) {
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
                                        context,
                                        "Sign in successful",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate(Route.Home.SignIn)
                                    viewModel.resetState()
                                }
                            }
                        }

                        composable(Route.Home.Welcome) {
                            BongaWelcome(navController = navController)
                        }

                        composable(Route.Home.SignIn) {
                            BongaSignIn(navController = navController) {
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
                                        context,
                                        "Sign in successful",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate(Route.Home.Category)
                                    viewModel.resetState()
                                }
                            }
                        }

                        composable(Route.Home.Category) {
                            BongaCategory(navController = navController){}
                        }



                        composable(Route.Home.Beverage) {
                            Beverages(navController = navController)
                        }

                        composable(Route.Home.Cart) {
                            CartScreen(
                                navController = navController,
                                cartItems = emptyList(),
                                cartItemViewModel = CartItemViewModel()
                            )
                        }

                        composable(Route.Home.ItemDetails) {
                            ItemDetailsScreen(
                                itemId = String.toString(),
                                navController = navController,
                            )
                        }
                    }
                }
            }
        }
    }
}
