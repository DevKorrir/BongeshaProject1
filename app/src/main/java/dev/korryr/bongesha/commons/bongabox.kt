package dev.korryr.bongesha.commons

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun BongaBox(
    modifier: Modifier,
    painter: Painter
){
    //Spacer(modifier = Modifier.height(8.dp))
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ){

        Image(
            contentDescription = null,
            painter = painter,
            modifier = Modifier
                .background(Color.Transparent)
                .size(34.dp)
        )
    }
}