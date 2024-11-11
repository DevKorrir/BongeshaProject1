package dev.korryr.bongesha.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.korryr.bongesha.ui.theme.orange28
import dev.korryr.bongesha.viewmodels.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chatViewModel: ChatViewModel = viewModel()) {
    val messages by chatViewModel.messages.collectAsState()
    val unreadMessages by chatViewModel.unreadMessages.collectAsState()
    var currentMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        chatViewModel.markMessagesAsRead()
    }

    Column(
        modifier = Modifier
            //.verticalScroll(rememberScrollState())
            .padding(16.dp)
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.BottomStart
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
                //.weight(1f)
        ) {
            Text("Unread Messages: $unreadMessages")
        }


        LazyColumn(
            modifier = Modifier
                .padding(bottom = 50.dp)
                //.weight(1f)
        ) {
            items(messages.size) { index ->
                val message = messages[index]
                if (message.isUser) {
                    UserMessage(message.message)
                } else {
                    BotMessage(message.message)
                }
            }
        }



        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            OutlinedTextField(
                value = currentMessage,
                onValueChange = { currentMessage = it },
                modifier = Modifier
                    .background(
                        shape = RoundedCornerShape(24.dp),
                        color = orange28
                    )
                    .clip(
                        RoundedCornerShape(24.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = orange28,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .background(
                        color = Color.Transparent,
                    ),
                onClick = {
                if (currentMessage.isNotEmpty()) {
                    chatViewModel.sendMessage(currentMessage)
                    currentMessage = ""
                }
            }) {
                Text("Send")
            }
        }}
    }
}

@Composable
fun UserMessage(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = message,
            modifier = Modifier
                .padding(8.dp)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(8.dp)
        )
    }
}

@Composable
fun BotMessage(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = message,
            modifier = Modifier
                .padding(8.dp)
                .background(
                    Color.LightGray,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        )
    }
}

