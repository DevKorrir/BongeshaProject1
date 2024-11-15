package dev.korryr.bongesha.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.korryr.bongesha.ui.theme.blue88

///////////////////////////////////////////////////////////////////////////
// pickup screen here , to mvvm later
///////////////////////////////////////////////////////////////////////////
@Composable
fun PickupScreen() {
    // not yet impliemented
    Text(
        text = "Delivered to Pickup-Station",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(8.dp))
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()

    ){

        Spacer(Modifier.height(50.dp))
        Text(
            textAlign = TextAlign.Center ,
            text = "Sorry coming soon...",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = blue88
        )
    }
}