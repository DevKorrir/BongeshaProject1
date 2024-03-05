package dev.korryr.bongesha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.screens.BongaSignIn
import dev.korryr.bongesha.screens.BongaSignUp
import dev.korryr.bongesha.screens.BongaWelcome
import dev.korryr.bongesha.screens.BongaFirebase
import dev.korryr.bongesha.ui.theme.BongeshaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            window?.statusBarColor = Color.Black.toArgb()

            BongeshaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent
                ){
                   /* Scaffold(
                        topBar = {
                            TopAppBar(
                                backgroudColor = Color.Red,
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
                    ){}*/
                    NavHost(
                        navController = navController,
                        startDestination = Route.Home.SignUp
                    ){
                        composable(Route.Home.SignUp){
                            BongaSignUp(navController = navController)
                        }

                        composable(Route.Home.Welcome){
                            BongaWelcome(navController = navController)
                        }

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
