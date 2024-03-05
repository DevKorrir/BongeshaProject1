package dev.korryr.bongesha.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.BongaBox
import dev.korryr.bongesha.commons.Bongatextfield
import dev.korryr.bongesha.commons.Route
import dev.korryr.bongesha.commons.bongabutton
import dev.korryr.bongesha.ui.theme.orange100
import java.net.URI

@Composable
fun BongaSignIn(
    navController: NavController
){
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clickable {
                    navController.popBackStack()
                }
                .border(
                    1.dp,
                    shape = RoundedCornerShape(24.dp),
                    color = Color.Transparent,
                )
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ){
            Icon(
                Icons.Default.ArrowBack,
                tint = Color.Gray,
                contentDescription = ""
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        //horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Image(
            painter = painterResource(id = R.drawable.growth_sticker),
            contentDescription = "")

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome back!",
                color = Color.Black,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Bongatextfield(
            label = "E-mail address",
            fieldDescription = "",
            input = email,
            hint = "Yourname@gmail.com",
            onChange = {
                       email = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            leading = painterResource(id = R.drawable.image_sec_icon)
        )
        Bongatextfield(
            label = "Password",
            isPassword = true,
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
            trailing = painterResource(id = R.drawable.hide_password)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Forgot password?",
                style = TextStyle(textDecoration = TextDecoration.Underline)
            )
        }

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            var ischecked by remember {
                mutableStateOf(false)
            }

            val checkedColor: Color = Color.Green
            val uncheckedColor: Color = Color.Transparent

            Checkbox(
                checked = ischecked,
               // colors = if (ischecked),
                onCheckedChange = {
                    ischecked = it
                },
                modifier = Modifier
                    .background(
                        color = if (ischecked) checkedColor else uncheckedColor,
                        shape = CircleShape
                    )
            )
            Text(
                text = "Remember me",
                fontSize = 16.sp,
                color = Color.Black,
                style = if (ischecked) androidx.compose.ui.text.TextStyle(textDecoration = TextDecoration.LineThrough) else
                    androidx.compose.ui.text.TextStyle(textDecoration = TextDecoration.None)
            )
        }

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

            Text(text = "or")

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
            BongaBox(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.google_icons),
            )

            BongaBox(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.facebook_icon),
            )

            BongaBox(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.apple_icon),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            bongabutton(
                modifier = Modifier.fillMaxWidth(),
                label = "Login",
                color = Color.White,
                buttonColor = Color.DarkGray
            ) {
                navController.navigate(Route.Home.Firebase)
            }

            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account?Sign up now."
                )

            }
        }
    }
}

@Composable
fun RAldoImagepicker(onImageSelected: (List<Uri>) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = {uriList ->
            onImageSelected(uriList)
        }
    )

    Button(
        onClick = { launcher.launch("image/*") }
    ) {
        Text("Select Image")
    }
}