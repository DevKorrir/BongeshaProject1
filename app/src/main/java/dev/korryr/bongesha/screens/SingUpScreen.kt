package dev.korryr.bongesha.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.BongaBox
import dev.korryr.bongesha.commons.BongaButton
import dev.korryr.bongesha.commons.Bongatextfield
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.ui.theme.green99
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.AuthState
import dev.korryr.bongesha.viewmodels.AuthViewModel

@Composable
fun BongaSignUp(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    onGoogleSignIn: () -> Unit,
    onSignIn: (String, String) -> Unit,
    onFacebookSignInClick: (() -> Unit)? = null
) {
    var displayName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPasswordError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var displayNameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    val authState by authViewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Register",
            fontWeight = FontWeight.ExtraBold,
            fontStyle = FontStyle.Normal,
            fontSize = 46.sp,
            textAlign = TextAlign.Center,
            color = orange28
        )

        Spacer(modifier = Modifier.height(16.dp))

        Bongatextfield(
            label = "Your name",
            isPassword = false,
            fieldDescription = "",
            //isValid = displayName.isNotEmpty(),
            input = displayName,
            trailing = null,
            leading = painterResource(id = R.drawable.user_person),
            hint = "Full Name",
            onChange = {
                displayName = it.capitalize()
                displayNameError = if (displayName.isBlank()) "Name cannot be empty" else ""
                       },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        if (displayNameError.isNotEmpty()) {
            Text(
                text = displayNameError,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Bongatextfield(
            label = "E-mail address",
            isPassword = false,
            fieldDescription = "",
            input = email,
            trailing = null,
            leading = painterResource(id = R.drawable.image_sec_icon),
            hint = "bongesha@gmail.com",
            onChange = {
                email = it
                emailError = if (email.isBlank()) "Email cannot be empty" else ""
                       },
            enabled = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            //isValid = email.isNotEmpty(),
        )
        if (email.isNotEmpty()){
            Text(
                text = emailError,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Bongatextfield(
            label = "Password",
            isPassword = true,
            fieldDescription = "",
            input = password,
            trailing = painterResource(id = R.drawable.ic_show_password),
            leading = painterResource(id = R.drawable.padlock),
            hint = "Enter your password",
            onChange = {
                password = it
                passwordError = if (!isValidPassword(password)) {
                    "Password must be at least 8 characters long, contain a number, a symbol, and both uppercase & lowercase letters."
                } else ""
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            )
        )
        if (passwordError.isNotEmpty()){
            Text(
                text = passwordError,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Bongatextfield(
            label = "Confirm Password",
            isPassword = true,
            fieldDescription = "",
            input = confirmPassword,
            trailing = painterResource(id = R.drawable.ic_show_password),
            leading = painterResource(id = R.drawable.padlock),
            hint = "Confirm your password",
            onChange = {
                confirmPassword = it
                confirmPasswordError = if (confirmPassword != password) "Passwords do not match." else ""
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )
        if (confirmPasswordError.isNotEmpty()) {
            Text(
                text = confirmPasswordError,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        } else if (confirmPassword.isNotEmpty() && confirmPassword == password) {
            Text(
                text = "Correct",
                color = green99,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        BongaButton(
            label = "Create an account",
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            buttonColor = orange28,
            enabled = displayName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty(),
            onClick = {
                when {
                    displayName.isBlank() -> {
                        displayNameError = "Name cannot be empty"
                    }

                    email.isBlank() -> {
                        emailError = "Email cannot be empty"
                    }

                    !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        emailError = "Enter a valid email address"
                    }

                    password.isBlank() -> {
                        passwordError = "Password cannot be empty"
                    }

                    !isValidPassword(password) -> {
                        passwordError =
                            "Password must be at least 8 characters long, contain a number, a symbol, and both uppercase & lowercase letters."
                    }

                    password != confirmPassword -> {
                        confirmPasswordError = "Passwords do not match."
                    }

                    else -> {
                        authViewModel.signUp(email, password, displayName)
                        navController.navigate(Route.Home.SIGN_IN)
                    }
                }
//                if (!isValidPassword(password)) {
//                    showPasswordError = true
//                    errorMessage = "Password must be at least 8 characters long, contain a number, a symbol, and both uppercase & lowercase letters."
//                } else if (password != confirmPassword) {
//                    showPasswordError = true
//                    errorMessage = "Password does not match."
//                } else {
//                    authViewModel.signUp(email, password, displayName)
//                    navController.navigate(Route.Home.Verification)
//                }
            }
        )

        Spacer(Modifier.height(10.dp))

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

            Text(text = "or sign up with")

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
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            //google button
            BongaBox(
                modifier = Modifier
                    .clickable { onGoogleSignIn() },
                painter = painterResource(id = R.drawable.google_icons)
            )

            //facebook button
            BongaBox(
                modifier = Modifier
                    .clickable { onFacebookSignInClick?.invoke() },
                painter = painterResource(id = R.drawable.facebook_icon)
            )
        }


        if (authState is AuthState.Loading) {
            CircularProgressIndicator(
                color = orange28,
                modifier = Modifier
                    .size(50.dp)
                    .padding(16.dp)
            )
        }

//        if (authState is AuthState.Error) {
//            Text(
//                text = (authState as AuthState.Error).message,
//                color = Color.Red,
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier.padding(vertical = 8.dp)
//            )
//        }

        if (authState is AuthState.Success) {
            LaunchedEffect(Unit) {
                navController.navigate(Route.Home.SIGN_IN)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Already have an account? ",
                style = MaterialTheme.typography.bodyMedium
            )

            TextButton(
                onClick = {
                    navController.navigate(Route.Home.SIGN_IN)
                }
            ) {
                Text(
                    text = "Login",
                    fontSize = 15.sp,
                    color = orange28
                )
            }
        }
    }
}

