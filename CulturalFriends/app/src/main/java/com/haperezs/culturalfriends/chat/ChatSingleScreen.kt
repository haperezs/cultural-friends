package com.haperezs.culturalfriends.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.haperezs.culturalfriends.Screen
import com.haperezs.culturalfriends.auth.AuthViewModel
import com.haperezs.culturalfriends.chat.components.ChatBubble
import com.haperezs.culturalfriends.finder.FinderViewModel
import com.haperezs.culturalfriends.translate.TranslateViewModel

@Composable
fun ChatSingleScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    chatViewModel: ChatViewModel,
    finderViewModel: FinderViewModel,
    translateViewModel: TranslateViewModel,
    chatId: String
) {
    val currentChat by chatViewModel.currentChat.collectAsStateWithLifecycle()
    val publicMarker by finderViewModel.publicMarker.collectAsStateWithLifecycle()
    val messages by chatViewModel.messages.collectAsStateWithLifecycle()
    val userId by authViewModel.userId.collectAsStateWithLifecycle()
    var messageText by remember { mutableStateOf("") }

    chatViewModel.fetchMessages(chatId)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            reverseLayout = true
        ) {
            items(messages) { message ->
                ChatBubble(
                    message = message,
                    isCurrentUser = message.sender == userId,
                    onClickTranslate = {
                        translateViewModel.updateInputText(message.text)
                        translateViewModel.updateSourceLanguage(currentChat.otherUserLanguage)
                        publicMarker?.let { translateViewModel.updateTargetLanguage(it.language) }
                        translateViewModel.translateText()
                        navController.navigate(Screen.TranslateScreen.route){
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                )
            }
        }

        HorizontalDivider()

        Row(
            modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Message...") },
                maxLines = 3,
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (messageText.isNotBlank()) {
                        chatViewModel.sendMessage(chatId, messageText)
                        messageText = ""
                    }
                }
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
        }
    }
}