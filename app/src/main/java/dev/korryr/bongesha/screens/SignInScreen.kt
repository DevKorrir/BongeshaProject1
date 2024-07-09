package dev.korryr.bongesha.screens

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.*
import dev.korryr.bongesha.ui.theme.orange28

private const val PREFS_NAME = "BongaPrefs"
private const val PREFS_KEY_SIGNED_IN = "isSignedIn"

@Composable
fun BongaSignIn(
    navController: NavController,
    onClick: () -> Unit,
    onForgotPassword: (String) -> Unit,
    onSignIn: (String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current


    // Check if the user is already signed in
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val isSignedIn = prefs.getBoolean(PREFS_KEY_SIGNED_IN, false)

    if (isSignedIn) {
        // Navigate to the home screen if the user is already signed in
        LaunchedEffect(Unit) {
            navController.navigate(Route.Home.Category) {
                popUpTo(Route.Home.SignIn) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .clickable {
                    navController.navigateUp()
                }
                .border(
                    1.dp,
                    shape = RoundedCornerShape(24.dp),
                    color = Color.Transparent
                )
                .background(
                    Color.Transparent,
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                tint = Color.Gray,
                contentDescription = ""
            )
        }
    }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Welcome back!",
            color = Color.Black,
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Text(
            text = "Sign in to continue",
            color = Color.Gray,
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(30.dp))

        Bongatextfield(
            label = "E-mail address",
            input = email,
            hint = "Yourname@gmail.com",
            onChange = { email = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            leading = painterResource(id = R.drawable.image_sec_icon),
            fieldDescription = ""
        )

        Spacer(modifier = Modifier.height(24.dp))

        Bongatextfield(
            label = "Password",
            isPassword = true,
            input = password,
            hint = "Enter your password",
            onChange = { password = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            leading = painterResource(id = R.drawable.padlock),
            trailing = painterResource(id = R.drawable.ic_show_password),
            fieldDescription =""
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            var isChecked by remember { mutableStateOf(false) }

            BongaCheckbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                shape = RoundedCornerShape(8.dp),
                checkedColor = orange28,
                uncheckedColor = Color.Transparent,
                size = 24.dp
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Remember me",
                fontSize = 16.sp,
                color = Color.Black,
                style = if (isChecked) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle(textDecoration = TextDecoration.None)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Forgot password?",
                fontSize = 16.sp,
                color = Color.Black,
                style = TextStyle(textDecoration = TextDecoration.Underline),
                modifier = Modifier
                    .clickable {
                        if (email.isNotEmpty()) {
                            onForgotPassword(email)
                        } else {
                            Toast
                                .makeText(context, "Please enter your email", Toast.LENGTH_SHORT)
                                .show()
                        }
                    //navController.navigate(Route.Home.ForgotPassword)
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1f),
                thickness = 1.dp,
                color = Color.LightGray
            )

            Text(text = "or continue with")

            HorizontalDivider(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
                thickness = 1.dp,
                color = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BongaBox(
                modifier = Modifier.clickable { onClick() },
                painter = painterResource(id = R.drawable.google_icons)
            )

            BongaBox(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.facebook_icon)
            )

            BongaBox(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.apple_icon)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        BongaButton(
            modifier = Modifier.fillMaxWidth(),
            label = "Login",
            color = Color.White,
            buttonColor = orange28,
            onClick = {
                //onSignIn(email, password)

                if (email.isNotBlank() && password.isNotBlank()) {
                    signInWithEmailAndPassword(email, password, context, navController)
                } else {
                    Toast.makeText(context, "Please enter valid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        ) /*{
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            with (prefs.edit()) {
                                putBoolean(PREFS_KEY_SIGNED_IN, true)
                                apply()
                            }
                            navController.navigate(Route.Home.Category) {
                                popUpTo(Route.Home.SignIn) { inclusive = true }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Sign in failed. Please check your credentials.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
        */

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Don't have an account? ",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Sign up",
                style = MaterialTheme.typography.bodyMedium.copy(color = orange28),
                modifier = Modifier.clickable {
                    navController.navigate(Route.Home.SignUp)
                }
            )
        }
    }
}

private fun signInWithEmailAndPassword(email: String, password: String, context: Context, navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Sign in successful", Toast.LENGTH_SHORT).show()
                // Navigate to your desired screen upon successful sign-in
                navController.navigate(Route.Home.Category)
                // Save the login state for one-time sign-in using SharedPreferences
                saveSignInState(context)
            } else {
                Toast.makeText(context, "Authentication failed please check your credentialst", Toast.LENGTH_SHORT).show()
            }
        }
}

private fun resetPassword(email: String, context: Context) {
    val auth = FirebaseAuth.getInstance()
    auth.sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Password reset email sent", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to send password reset email", Toast.LENGTH_SHORT).show()
            }
        }
}


private fun saveSignInState(context: Context) {
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit {
        putBoolean(PREFS_KEY_SIGNED_IN, true)
    }
}

@Composable
fun CheckSignInState(navController: NavController) {
    val context = LocalContext.current
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val isSignedIn = prefs.getBoolean(PREFS_KEY_SIGNED_IN, false)

    if (isSignedIn) {
        navController.navigate(Route.Home.Category)
    }
}
