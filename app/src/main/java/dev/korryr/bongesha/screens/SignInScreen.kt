package  dev.korryr.bongesha.screens

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.BongaBox
import dev.korryr.bongesha.commons.BongaButton
import dev.korryr.bongesha.commons.BongaCheckbox
import dev.korryr.bongesha.commons.Bongatextfield
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.AuthState
import dev.korryr.bongesha.viewmodels.AuthViewModel

private const val PREFS_NAME = "BongaPrefs"
private const val PREFS_KEY_SIGNED_IN = "isSignedIn"

@Composable
fun BongaSignIn(
    onGoogleSignIn: (String) -> Unit,
    navController: NavController,
    onForgotPassword: (String) -> Unit,
    onSignIn: (String, String) -> Unit,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val callbackManager = remember { CallbackManager.Factory.create() }
    val authState by authViewModel.authState.collectAsState()


    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val isSignedIn = prefs.getBoolean(PREFS_KEY_SIGNED_IN, false)

    if (isSignedIn) {
        // Navigate to the home screen if the user is already signed in
        LaunchedEffect(Unit) {
            navController.navigate(Route.Home.HOME) {
                popUpTo(Route.Home.SIGN_IN) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ){
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable {
                        navController.navigate(Route.Home.SIGN_UP)
                    }
                    .border(
                        1.dp,
                        shape = RoundedCornerShape(24.dp),
                        color = Color.LightGray
                    )
                    .background(
                        Color.Transparent,
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(30.dp),
                    contentDescription = ""
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Welcome back",
            color = Color.Black,
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Login in to your account",
            color = Color.Gray,
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Email input field
        Bongatextfield(
            label = "E-mail address",
            fieldDescription = "",
            input = email,
            leading = painterResource(id = R.drawable.image_sec_icon),
            hint = "Yourname@gmail.com",
            onChange = { email = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Password input field
        Bongatextfield(
            label = "Password",
            isPassword = true,
            fieldDescription ="",
            input = password,
            trailing = painterResource(id = R.drawable.ic_show_password),
            leading = painterResource(id = R.drawable.padlock),
            hint = "Enter your password",
            onChange = { password = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        // Handle error and display an error message if there is one
        if (authState is AuthState.Error) {
            Text(
                text = (authState as AuthState.Error).message,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Forgot password link
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
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

            TextButton(
                onClick = {
                    navController.navigate(Route.Home.FORGOT_PASSWORD)
                }
            ) {
                Text(
                    text = "Forgot password?",
                    fontSize = 16.sp,
                    color = orange28,
                    style = TextStyle(textDecoration = TextDecoration.Underline),
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Email/Password sign-in button
        BongaButton(
            modifier = Modifier.fillMaxWidth(),
            label = "Sign In",
            color = Color.White,
            buttonColor = orange28,
            onClick = {
                // Validate that both fields are not empty
                if (email.isBlank()) {
                    Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
                } else if (password.isBlank()) {
                    Toast.makeText(context, "Please enter your password", Toast.LENGTH_SHORT).show()
                } else {
                    // Proceed with sign-in
                    onSignIn(email, password)
                }
            }
        )

        Spacer(Modifier.height(12.dp))

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

        // Google and Facebook Sign-In options
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                // Handle the result from the Google Sign-In intent
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.let { data ->
                        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                        try {
                            val account = task.getResult(ApiException::class.java)
                            account?.let {
                                // Now handle the Google sign-in, e.g., pass the account to your ViewModel
                                authViewModel.signInWithGoogle(it.idToken ?: "", navController)
                            }
                        } catch (e: ApiException) {
                            Toast.makeText(context, "Google sign-in failed: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

            BongaBox(
                modifier = Modifier.clickable {
                    val googleSignInClient = GoogleSignIn.getClient(
                        context,
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(context.getString(R.string.web_client_id))
                            .requestEmail()
                            .build()
                    )
                    val signInIntent = googleSignInClient.signInIntent
                    launcher.launch(signInIntent)
                },
                painter = painterResource(id = R.drawable.google_icons)
            )

            BongaBox(
                modifier = Modifier.clickable {
                    LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                        override fun onSuccess(result: LoginResult) {
                            // Handle successful Facebook login
                            val credential = FacebookAuthProvider.getCredential(
                                result.accessToken.token
                            )
                            FirebaseAuth.getInstance().signInWithCredential(credential)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        navController.navigate(Route.Home.HOME)
                                    } else {
                                        Toast.makeText(context, "Facebook sign-in failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }

                        override fun onCancel() {
                            // Handle Facebook login cancellation
                            Toast.makeText(context, "Facebook sign-in canceled", Toast.LENGTH_LONG).show()
                        }

                        override fun onError(error: FacebookException) {
                            // Handle Facebook login errors
                            Toast.makeText(context, "Facebook sign-in failed: ${error.message}", Toast.LENGTH_LONG).show()
                        }
                    })

                },
                painter = painterResource(id = R.drawable.facebook_icon)
            )

        }

        Spacer(modifier = Modifier.weight(1f))

        // Link to Sign Up
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Don't have an account? ",
                fontSize = 16.sp,
            )
            TextButton(
                onClick = {
                    navController.navigate(Route.Home.SIGN_UP)
                }
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 15.sp,
                    color = orange28,
                )
            }
        }
    }
}



