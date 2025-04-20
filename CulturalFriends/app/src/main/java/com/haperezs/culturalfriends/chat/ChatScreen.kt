package com.haperezs.culturalfriends.chat

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.haperezs.culturalfriends.navigation.Screen
import com.haperezs.culturalfriends.auth.AuthViewModel
import com.haperezs.culturalfriends.chat.components.ChatRequestRow
import com.haperezs.culturalfriends.chat.components.ChatRow

@Composable
fun ChatScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
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

