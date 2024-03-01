package dev.korryr.bongesha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.screens.BongaSignUp
import dev.korryr.bongesha.screens.BongaWelcome
import dev.korryr.bongesha.ui.theme.BongeshaTheme
import dev.korryr.bongesha.ui.theme.red100

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
                    }
                   /* val firebaseDatabase = FirebaseDatabase.getInstance();
                    val databaseReference = firebaseDatabase.getReference("EmployeeInfo");
                    firebaseUI(LocalContext.current, databaseReference)*/
                }
            }
        }
    }
}
