package dev.korryr.bongesha.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

        Spacer(modifier = Modifier.height(38.dp))

        Image(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.Black)
                .size(250.dp),
            painter = painterResource(id = R.drawable.signup_image),
            contentDescription = "",

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
        //name textfield
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
            ),
            trailingIcon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Person",
                    tint = orange100
                )
            },
        )

        Spacer(modifier = Modifier.height(24.dp))
            //textfield for emaill
        Bongatextfield(
            isPassword = false,
            isValid = true,
            errorMessage = "Please enter valid email",
            label = "E-mail address",
            fieldDescription = "",
            input = email,
            hint = "bongesha@gmail.com",
            enabled = true,
            onChange = {
                email = it
            },
            trailingIcon = {
                           Icon(
                               Icons.Default.MailOutline,
                               contentDescription = "Email",
                               tint = orange100
                           )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(24.dp))
//password textfield
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
            ),
            trailingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = "Password",
                    tint = orange100
                )
            },
        )
    }
}