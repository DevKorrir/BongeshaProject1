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
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.AuthState
import dev.korryr.bongesha.viewmodels.AuthViewModelMail
import kotlinx.coroutines.Job

@Composable
fun BongaSignUp(
    navController: NavController,
    authViewModel: AuthViewModelMail = viewModel(),
    function: () -> Job
) {
    var yourname by rememberSaveable { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPasswordError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val authState by authViewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Register",
            fontWeight = FontWeight.ExtraBold,
            fontStyle = FontStyle.Normal,
            fontSize = 56.sp,
            textAlign = TextAlign.Center,
            color = orange28
        )

        Spacer(modifier = Modifier.height(16.dp))

        Bongatextfield(
            label = "Your name",
            isPassword = false,
            fieldDescription = "",
            isValid = true,
            input = yourname,
            trailing = null,
            leading = painterResource(id = R.drawable.user_person),
            hint = "Username",
            onChange = { yourname = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Bongatextfield(
            label = "E-mail address",
            isPassword = false,
            fieldDescription = "",
            isValid = true,
            input = email,
            trailing = null,
            leading = painterResource(id = R.drawable.image_sec_icon),
            hint = "bongesha@gmail.com",
            onChange = { email = it },
            enabled = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

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
                showPasswordError = false
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            )
        )

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
                showPasswordError = false
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                    .clickable {  },
                painter = painterResource(id = R.drawable.google_icons)
            )

            //facebook button
            BongaBox(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.facebook_icon)
            )

            //apple button
            BongaBox(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.apple_icon)
            )
        }

        if (showPasswordError) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
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

        if (authState is AuthState.Error) {
            Text(
                text = (authState as AuthState.Error).message,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        if (authState is AuthState.Success) {
            LaunchedEffect(Unit) {
                navController.navigate(Route.Home.SignIn)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        BongaButton(
            label = "Create an account",
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            buttonColor = orange28
        ) {
            if (!isValidPassword(password)) {
                showPasswordError = true
                errorMessage = "Password must be at least 8 characters long, contain a number, a symbol, and both uppercase & lowercase letters."
            } else if (password != confirmPassword) {
                showPasswordError = true
                errorMessage = "Password does not match."
            } else {
                authViewModel.signUp(email, password)
                //navController.navigate(Route.Home.SignIn)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Already have an account? ",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Sign in",
                style = MaterialTheme.typography.bodyMedium.copy(color = orange28),
                modifier = Modifier.clickable {
                    navController.navigate(Route.Home.SignIn)
                }
            )
        }
    }
}

private fun isValidPassword(password: String): Boolean {
    val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
    val pattern = Regex(passwordPattern)
    return pattern.matches(password)
}
