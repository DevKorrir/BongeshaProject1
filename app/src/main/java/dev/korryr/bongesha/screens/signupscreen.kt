package dev.korryr.bongesha.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
//import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.BongaBox
import dev.korryr.bongesha.commons.Bongatextfield
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.commons.bongabutton
import dev.korryr.bongesha.ui.theme.orange28

@Composable
fun BongaSignUp(
    navController: NavController,
    onClick: () -> Unit,
){

    var yourname by rememberSaveable {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var showPasswordError by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        Image(
            modifier = Modifier
                //.clip(CircleShape)
                .background(Color.Transparent)
                .size(250.dp),
            painter = painterResource(id = R.drawable.problem_solved),
            contentDescription = "",

        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text ="Register",
            fontWeight = FontWeight.ExtraBold,
            fontStyle = FontStyle.Normal,
            fontSize = 56.sp,
            textAlign = TextAlign.Center,
            color = orange28
        )

        Spacer(modifier = Modifier.height(16.dp))

        //name textfield
        Bongatextfield(
            isPassword = false,
            isValid = true,
            trailing = null,
            errorMessage = "Please enter your name",
            label = "Your name",
            fieldDescription = "",
            input = yourname,
            hint = "Amos",
            onChange = {
                yourname = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            leading = painterResource(id = R.drawable.user_person)
        )

        Spacer(modifier = Modifier.height(8.dp))

        //email textfield

        Bongatextfield(
            isPassword = false,
            isValid = true,
            trailing = null,
            errorMessage = "Please enter valid email",
            label = "E-mail address",
            fieldDescription = "",
            input = email,
            hint = "bongesha@gmail.com",
            enabled = true,
            onChange = {
                email = it
            },
            leading = painterResource(id = R.drawable.image_sec_icon),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(8.dp))
//password textfield
        Bongatextfield(
            label = "Password",
            isPassword = true,
            trailing = painterResource(id = R.drawable.ic_show_password),
            fieldDescription = "",
            input = password,
            hint = "Enter your password",
            //isError = showPasswordError,
            onChange = {
                password = it
                showPasswordError = false
                       },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Bongatextfield(
            label = "Confirm Password",
            isPassword = true,
            trailing = painterResource(id = R.drawable.ic_show_password),
            fieldDescription = "",
            input = confirmPassword,
            hint = "Confirm your password",
            onChange = {
                confirmPassword = it
                showPasswordError = false
                       },
            //isError = showPasswordError,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
        )


        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1f),
                thickness = 1.dp,
                color = Color.LightGray
            )

            Text(text = "or sign up with")

            Divider(
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
                        .clickable {
                            onClick()
                        },
                    painter = painterResource(id = R.drawable.google_icons),

                    )

            //facebook button
            BongaBox(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.facebook_icon),
            )
            //apple button
            BongaBox(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.apple_icon),
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        if (showPasswordError) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        bongabutton(
            label = "Create an account",
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            buttonColor = orange28,
        ) {
            //validation
            if (!isValidPassword(password)) {
                showPasswordError = true
                errorMessage = "Password must be at least 8 characters long, contain a number, a symbol, and both uppercase & lowercase letters."
            } else if (password != confirmPassword) {
                showPasswordError = true
                errorMessage = "Password does not match."
            } else {
                navController.navigate(Route.Home.SignIn)
            }
           // navController.navigate(Route.Home.SignIn)
        }
        Row {
            Text(
                text = "Already have an account?"
            )
        }
    }
}