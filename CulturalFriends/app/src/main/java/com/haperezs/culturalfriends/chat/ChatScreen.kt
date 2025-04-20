package com.haperezs.culturalfriends.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.haperezs.culturalfriends.Screen
import com.haperezs.culturalfriends.auth.AuthViewModel
import com.haperezs.culturalfriends.chat.components.ChatRequestRow
import com.haperezs.culturalfriends.chat.components.ChatRow
import com.haperezs.culturalfriends.utils.formatTimestamp

@Composable
fun ChatScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    chatViewModel: ChatViewModel,
) {
    val chats by chatViewModel.chats.collectAsStateWithLifecycle()
    val chatRequests by chatViewModel.chatRequests.collectAsStateWithLifecycle()
    val userId by authViewModel.userId.collectAsStateWithLifecycle()

    LazyColumn {
        items(chatRequests) { chatRequest ->
            ChatRequestRow(
                chatRequest = chatRequest,
                onClickDeny = {
                    chatViewModel.denyChatRequest(chatRequest)
                },
                onClickAccept = {
                    chatViewModel.acceptChatRequest(chatRequest)
                }
            )
            HorizontalDivider()
        }
        items(chats) { chat ->
            ChatRow(
                chat = chat,
                userId = userId,
                onClick = {
                    chatViewModel.updateCurrentChat(chat)
                    navController.navigate(Screen.ChatSingleScreen.createRoute(chat.id))
                }
            )
            HorizontalDivider()
        }
    }
}

