package dev.korryr.bongesha.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun BongaRow(
    modifier: Modifier,
    imageVectorleading: ImageVector? = null,
    imageVectortrailing: ImageVector? = null,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (imageVectorleading != null) {
                Icon(
                    imageVector = imageVectorleading,
                    contentDescription = ""
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