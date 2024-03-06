package dev.korryr.bongesha.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun BongaFirebase(
    navController: NavController
){

    var imageUriList by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }

    Column(
modifier = Modifier
    .padding(24.dp)
    //.verticalScroll(rememberScrollState())
    ) {
        AldoImagepicker {
            imageUriList = it

        }

        Column(
            //modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
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