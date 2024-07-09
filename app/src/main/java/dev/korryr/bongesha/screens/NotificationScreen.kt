package dev.korryr.bongesha.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.korryr.bongesha.viewmodels.Notification
import dev.korryr.bongesha.viewmodels.NotificationViewModel

@Composable
fun NotificationIcon(

) {





}

@Composable
fun NotificationList(notificationViewModel: NotificationViewModel = viewModel()) {
    val notifications by notificationViewModel.notifications.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        items(notifications) { notification ->
            NotificationRow(
                notification = notification,
                onClick = { notificationViewModel.markAsRead(notification.id) }
            )
            Divider()
        }
    }
}

@Composable
fun NotificationRow(
    notification: Notification,
    onClick: () -> Unit
) {
    val textStyle = if (notification.isRead) {
        MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
    } else {
        MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Text(text = notification.message, style = textStyle)
    }
}
