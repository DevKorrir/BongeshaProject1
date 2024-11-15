package dev.korryr.bongesha.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.libraries.places.api.net.kotlin.isOpenRequest
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.viewmodels.AuthViewModel

@Composable
fun GeneralSettingsScreen(
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


@Composable
fun DeleteAccountRequestScreen(
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val user = authViewModel.auth.currentUser // Get the current user
    var isOpenRequesting by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Replace delete button with a "Request Account Deletion" button
        Button(
            onClick = {
                sendWhatsAppMessage(
                    context,
                    user?.displayName ?: "User"
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            )
        ) {
            if (isOpenRequesting) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = "Request Account Deletion",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

// Function to send a pre-filled WhatsApp message
private fun sendWhatsAppMessage(context: Context, userName: String) {
    val phoneNumber = "+254719227769" // begin with country code
    val message = "Bongesha customer \"$userName\" wants to delete their account."

    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("https://wa.me/$phoneNumber/?text=${Uri.encode(message)}")
    }

    // Check if WhatsApp is installed
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_LONG).show()
    }
}

