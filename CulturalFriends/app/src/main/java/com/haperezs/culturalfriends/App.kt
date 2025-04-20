package com.haperezs.culturalfriends

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.haperezs.culturalfriends.auth.AuthViewModel
import com.haperezs.culturalfriends.chat.ChatViewModel
import com.haperezs.culturalfriends.finder.FinderViewModel
import com.haperezs.culturalfriends.navigation.AppNavHost
import com.haperezs.culturalfriends.navigation.NavBar
import com.haperezs.culturalfriends.navigation.Screen
import com.haperezs.culturalfriends.navigation.TopBar
import com.haperezs.culturalfriends.translate.TranslateViewModel

class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val badge: Boolean,
    val route: String
)

@Composable
fun App() {
    val navController = rememberNavController()
    val authViewModel = viewModel<AuthViewModel>()
    val chatViewModel = viewModel<ChatViewModel>()
    val finderViewModel = viewModel<FinderViewModel>()
    val translateViewModel = viewModel<TranslateViewModel>()

    val currentChat by chatViewModel.currentChat.collectAsStateWithLifecycle()
    val user by authViewModel.authState.collectAsStateWithLifecycle()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // If user is not logged in, navigate to Auth Screen
    val startDestination = if (user != null) Screen.FinderScreen.route else Screen.AuthScreen.route

    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                currentRoute = currentRoute?: "",
                currentChat = currentChat
            )
        },
        bottomBar = {
            if (currentRoute != Screen.AuthScreen.route) {
                NavBar(
                    navController = navController,
                    currentRoute = currentRoute?: ""
                )
            }
        },
        content = { innerPadding ->
            AppNavHost(
                navController = navController,
                authViewModel = authViewModel,
                chatViewModel = chatViewModel,
                finderViewModel = finderViewModel,
                translateViewModel = translateViewModel,
                startDestination = startDestination,
                innerPadding = innerPadding
            )
        }
    )
}