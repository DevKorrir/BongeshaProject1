package dev.korryr.bongesha.commons

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.korryr.bongesha.ui.theme.orange100

@Composable
fun BongaBox(
    modifier: Modifier,
    painter: Painter
){
    //Spacer(modifier = Modifier.height(8.dp))
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            //.clip(RoundedCornerShape(12.dp))
            .border(
                1.dp, color = orange100,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = Color.Transparent,
                //shape = RoundedCornerShape(16.dp)
            )
            .size(54.dp),

    ){

        Image(
            contentDescription = null,
            painter = painter,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .background(Color.Transparent)
                .size(34.dp)
        )
    }
}