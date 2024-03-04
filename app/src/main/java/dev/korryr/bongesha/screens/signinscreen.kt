package dev.korryr.bongesha.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.Bongatextfield
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
            //.verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Image(
            painter = painterResource(id = R.drawable.growth_sticker),
            contentDescription = "")

        Text(
            text = "Welcome back!",
            color = Color.Black,
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold
        )

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
                //.padding(horizontal = 170.dp),
        ) {
            Text(text = "Forgot password")
        }

        Row (
            modifier = Modifier.padding(24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var ischecked by remember {
                mutableStateOf(false)
            }
            Checkbox(
                checked = ischecked,
               // colors = if (ischecked),
                onCheckedChange = {
                    ischecked = it
                }
            )
            Text(
                text = "Remember me",
                fontSize = 16.sp,
                color = Color.Black,
                style = if (ischecked) androidx.compose.ui.text.TextStyle(textDecoration = TextDecoration.LineThrough) else
                    androidx.compose.ui.text.TextStyle(textDecoration = TextDecoration.None)
            )
        }

        var imageUriList by remember {
            mutableStateOf<List<Uri>>(emptyList())
        }

        Column(
            //modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            AldoImagepicker {
                imageUriList = it

            }

            imageUriList.forEach{
                Image(
                    painter = rememberAsyncImagePainter(model = it),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun AldoImagepicker(onImageSelected: (List<Uri>) -> Unit) {
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