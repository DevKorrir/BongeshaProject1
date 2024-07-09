package dev.korryr.bongesha.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Notification(val id: String, val message: String, var isRead: Boolean)

class NotificationViewModel : ViewModel() {
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> get() = _notifications

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> get() = _unreadCount

    init {
        // Initialize with some sample data
        _notifications.value = listOf(
            Notification("1", "New order received", false),
            Notification("2", "Payment received", true),
            Notification("3", "New message from customer", false)
        )
        updateUnreadCount()
    }

    private fun updateUnreadCount() {
        _unreadCount.value = _notifications.value.count { !it.isRead }
    }

    fun markAsRead(notificationId: String) {
        _notifications.value = _notifications.value.map {
            if (it.id == notificationId) it.copy(isRead = true) else it
        }
        updateUnreadCount()
    }
}
