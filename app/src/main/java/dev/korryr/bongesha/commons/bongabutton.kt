package dev.korryr.bongesha.commons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.korryr.bongesha.ui.theme.orange100

@Composable
fun bongabutton(
    modifier: Modifier = Modifier,
    label: String,
    showArrow: Boolean = true,
    onClick: () -> Unit
){
    Button(
        border = BorderStroke(1.dp, color = orange100),
        shape = MaterialTheme.shapes.large.copy(all = CornerSize(16.dp)),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(label)
            .height(56.dp)
        ){
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            if (showArrow){
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "go to signup",
                    //tint = Color.White
                )
            }
        }

    }
}