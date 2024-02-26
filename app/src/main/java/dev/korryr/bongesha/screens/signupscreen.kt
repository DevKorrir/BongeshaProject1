package dev.korryr.bongesha.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.korryr.bongesha.commons.Bongatextfield
import androidx.navigation.NavController
import dev.korryr.bongesha.R
import dev.korryr.bongesha.ui.theme.orange100

@Composable
fun BongaSignUp(
    navController: NavController
){

    var yourname by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            modifier = Modifier
                .size(250.dp),
            painter = painterResource(id = R.drawable.signup_image),
            contentDescription = ""
        )

        Spacer(modifier = Modifier.height(56.dp))

        Text(
            text ="Become a member!",
            fontWeight = FontWeight.ExtraBold,
            fontStyle = FontStyle.Normal,
            fontSize = 56.sp,
            textAlign = TextAlign.Center,
            color = orange100
        )
        Bongatextfield(
            isPassword = false,
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
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Bongatextfield(
            isPassword = false,
            label = "E-mail address",
            fieldDescription = "",
            input = email,
            hint = "bongesha@gmail.com",
            onChange = {
                email = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Bongatextfield(
            isPassword = true,
            label = "Password",
            fieldDescription = "",
            input = password,
            hint = "Enter your password",
            onChange = {
                password = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )
    }
}