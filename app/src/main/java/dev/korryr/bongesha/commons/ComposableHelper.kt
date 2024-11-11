package dev.korryr.bongesha.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.korryr.bongesha.ui.theme.orange28


// Helper Composable for Badge Box
@Composable
fun BadgeBoxa(unreadCount: Int) {
    Box(
        modifier = Modifier
            .size(14.dp)
            .background(orange28, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = unreadCount.toString(),
            color = Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

