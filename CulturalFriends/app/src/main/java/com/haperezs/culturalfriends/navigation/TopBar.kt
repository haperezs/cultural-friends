package com.haperezs.culturalfriends.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.haperezs.culturalfriends.model.Chat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    currentRoute: String,
    currentChat: Chat
) {
    // Routes where back arrow is shown
    val backArrowRoutes = listOf(Screen.ChatSingleScreen.route, Screen.SettingsScreen.route)
    val canNavigateBack = currentRoute in backArrowRoutes

    val title = when (currentRoute) {
        Screen.AuthScreen.route -> "Cultural Friends"
        Screen.ChatScreen.route -> "Chats"
        Screen.ChatSingleScreen.route -> currentChat.otherUserName
        Screen.FinderScreen.route -> "Finder"
        Screen.SettingsScreen.route -> "Settings"
        Screen.TranslateScreen.route -> "Translate"
        else -> "Cultural Friends"
    }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title
            )
        },
        actions = {
            IconButton(
                onClick = {
                    navController.navigate(Screen.SettingsScreen.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Go to settings"
                )
            }
        },
        navigationIcon = {
            if (canNavigateBack){
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}