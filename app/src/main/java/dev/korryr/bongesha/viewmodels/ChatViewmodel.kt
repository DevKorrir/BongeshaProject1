package dev.korryr.bongesha.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ChatMessage(val message: String, val isUser: Boolean)

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> get() = _messages

    private val _unreadMessages = MutableStateFlow(0)
    val unreadMessages: StateFlow<Int> get() = _unreadMessages

    private val botResponses = listOf(
        "Hi! How can I help you today?",
        "Thank you for reaching out to Bongesha bot.",
        "I'm here to assist you with your queries.",
        "Can you please provide more details?",
        "Your message is important to us."
    )

    fun sendMessage(message: String) {
        _messages.value += ChatMessage(message, isUser = true)
        receiveBotMessage(message)
    }

    private fun receiveBotMessage(userMessage: String) {
        val botMessage = generateBotResponse(userMessage)
        _messages.value += ChatMessage(botMessage, isUser = false)
        _unreadMessages.value += 1
    }

    private fun generateBotResponse(userMessage: String): String {
        // Simple response generation based on user message length for demonstration
        return when {
            userMessage.contains("help", ignoreCase = true) -> "How can I assist you with your issue?"
            userMessage.contains("price", ignoreCase = true) -> "Please provide the item name to get the price details."
            else -> botResponses.random()
        }
    }

    fun markMessagesAsRead() {
        _unreadMessages.value = 0
    }
}
