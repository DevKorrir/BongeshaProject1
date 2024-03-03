package dev.korryr.bongesha.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.Bongatextfield

@Composable
fun BongaSignIn(
    navController: NavController
){
    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.problem_solved),
            contentDescription = "")
        Text(text = "Welcome back!")
        Bongatextfield(
            label = "",
            fieldDescription = "",
            input = "",
            hint = "",
            onChange = {}
        )
        Bongatextfield(
            label = "",
            fieldDescription = "",
            input = "",
            hint = "",
            onChange = {}
        )
    }
}