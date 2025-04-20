package com.haperezs.culturalfriends.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.haperezs.culturalfriends.auth.AuthScreen
import com.haperezs.culturalfriends.auth.AuthViewModel
import com.haperezs.culturalfriends.chat.ChatScreen
import com.haperezs.culturalfriends.chat.ChatSingleScreen
import com.haperezs.culturalfriends.chat.ChatViewModel
import com.haperezs.culturalfriends.finder.FinderScreen
import com.haperezs.culturalfriends.finder.FinderViewModel
import com.haperezs.culturalfriends.settings.SettingsScreen
import com.haperezs.culturalfriends.translate.TranslateScreen
import com.haperezs.culturalfriends.translate.TranslateViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    chatViewModel: ChatViewModel,
    finderViewModel: FinderViewModel,
    translateViewModel: TranslateViewModel,
    startDestination: String,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Screen.AuthScreen.route) {
            AuthScreen(
                navController = navController,
                authViewModel = authViewModel,
                finderViewModel = finderViewModel
            )
        }
        composable(Screen.ChatScreen.route) {
            ChatScreen(
                navController = navController,
                authViewModel = viewModel(),
                chatViewModel = chatViewModel
            )
        }
        composable(
            route = Screen.ChatSingleScreen.route,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")
            ChatSingleScreen(
                navController = navController,
                authViewModel = viewModel(),
                chatViewModel = chatViewModel,
                finderViewModel = viewModel(),
                translateViewModel = translateViewModel,
                chatId = chatId!!
            )
        }
        composable(Screen.FinderScreen.route) {
            FinderScreen(
                chatViewModel = chatViewModel,
                finderViewModel = viewModel(),
                translateViewModel = translateViewModel
            )
        }
        composable(Screen.SettingsScreen.route) {
            SettingsScreen(
                navController = navController,
                authViewModel = authViewModel,
                finderViewModel = viewModel(),
                translateViewModel = translateViewModel
            )
        }
        composable(Screen.TranslateScreen.route) {
            TranslateScreen(
                translateViewModel = translateViewModel
            )
        }
    }
}