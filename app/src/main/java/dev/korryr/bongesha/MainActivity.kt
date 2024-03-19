package dev.korryr.bongesha

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import dev.korryr.bongesha.screens.BongaFirebase
import dev.korryr.bongesha.ui.theme.BongeshaTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {

            val navController = rememberNavController()
            val viewModel = viewModel<SignInViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            window?.statusBarColor = Color.Black.toArgb()

            BongeshaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent
                ){
                    /*
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                modifier = Modifier
                                    .background(
                                        Color.Black
                                    ),
                                //backgroundColor = Color.Red,
                              title ={
                                  Text(
                                      text = "Bonga",
                                      modifier = Modifier.fillMaxWidth(),
                                      textAlign = TextAlign.Center,
                                      color = Color.Blue,
                                  )
                              }
                            )
                        }
                    ){}
                    
                     */
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = { result ->
                            if(result.resultCode == RESULT_OK) {
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
                    ){
                        composable(Route.Home.SignUp){

                            BongaSignUp(navController = navController){
                                lifecycleScope.launch {
                                    val signInIntentSender = googleAuthUiClient.signIn()
                                    launcher.launch(
                                        IntentSenderRequest.Builder(
                                            signInIntentSender ?: return@launch
                                        ).build()
                                    )
                                }
                            }

                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if(state.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in successful",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate(Route.Home.SignIn)
                                    viewModel.resetState()
                                }
                            }
                        }

                       // composable(Route.Home.Welcome){
                         //   BongaWelcome(navController = navController)
                       // }

                        composable(Route.Home.SignIn){
                            BongaSignIn(navController = navController)
                        }

                        composable(Route.Home.Firebase){
                            BongaFirebase(navController = navController)
                        }
                    }
                   /* val firebaseDatabase = FirebaseDatabase.getInstance();
                    val databaseReference = firebaseDatabase.getReference("EmployeeInfo");
                    firebaseUI(LocalContext.current, databaseReference)*/
                }
            }
        }
    }
}
