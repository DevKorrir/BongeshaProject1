package dev.korryr.bongesha.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.viewmodels.AuthViewModel

@Composable
fun UserSettingsScreen(
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    var isDeleting by remember { mutableStateOf(false) } // Loading state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Account deletion button
        Button(
            onClick = {
                isDeleting = true
                authViewModel.deleteUserAccount { isDeleted ->
                    isDeleting = false
                    if (isDeleted) {
                        Toast.makeText(context, "Account deleted successfully", Toast.LENGTH_LONG).show()
                        navController.navigate(Route.Home.SIGN_IN) {
                            popUpTo(Route.Home.HOME) { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "Failed to delete account", Toast.LENGTH_LONG).show()
                    }
                }
            },
            enabled = !isDeleting,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            )
        ) {
            if (isDeleting) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(text = "Delete Account", color = Color.White)
            }
        }
    }
}
