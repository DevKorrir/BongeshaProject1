package dev.korryr.bongesha.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.korryr.bongesha.R
import dev.korryr.bongesha.commons.Route


@Composable
fun BongaAccSettings(
    navController: NavController
) {
    Column (
        modifier = Modifier
            .padding(12.dp)
            .verticalScroll(rememberScrollState())
    ){
        Row (
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            Box (
                modifier = Modifier
                    .clickable(
                        onClick = {
                            navController.navigateUp()
                        }
                    )
            ){
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(50.dp))

            Text(
                text = "Account Settings",
                fontWeight = FontWeight(weight = 700)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        AccountSettingScreen(
            text = "Name",
            painter = painterResource(id = R.drawable.setting)
        )
        Spacer(modifier = Modifier.height(12.dp))

        AccountSettingScreen(
            text = "Change address",
            painter = painterResource(id = R.drawable.setting)
        )
        Spacer(modifier = Modifier.height(12.dp))

        AccountSettingScreen(
            text = "Change mobile number",
            painter = painterResource(id = R.drawable.setting)
        )
        Spacer(modifier = Modifier.height(12.dp))

        AccountSettingScreen(
            text = "Issue with account information",
            painter             = painterResource(id = R.drawable.setting)
        )
        Spacer(modifier = Modifier.height(12.dp))

        AccountSettingScreen(
            onClick = {
                navController.navigate(Route.Home.DELETE_ACCOUNT)
            },
            text = "Delete account",
            painter = painterResource(id = R.drawable.setting)
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}
@Composable
fun AccountSettingScreen(
    text: String,
    painter: Painter,
    onClick: () -> Unit = {}
) {
    Column (
        modifier = Modifier
            .clickable(
                onClick = onClick
            )
    ){
        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row {
            Text(
                text = text
            )
            Spacer(modifier = Modifier.weight(1f))
            Box {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider()
    }

}