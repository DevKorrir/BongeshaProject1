package dev.korryr.bongesha.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun BongaRow(
    modifier: Modifier,
    imageVectorleading: Painter? = null,
    imageVectortrailing: ImageVector? = null,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Row(
            modifier = modifier
                .clickable(onClick = onClick),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (imageVectorleading != null) {
                Icon(
                    painter = imageVectorleading,
                    contentDescription = "",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(30.dp)
                        .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            if (imageVectortrailing != null) {
                Icon(
                    imageVector = imageVectortrailing,
                    contentDescription = ""
                )
            }
        }
    }
}