package com.haperezs.culturalfriends

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.haperezs.culturalfriends.auth.AuthScreen
import com.haperezs.culturalfriends.auth.AuthViewModel
import com.haperezs.culturalfriends.chat.ChatScreen
import com.haperezs.culturalfriends.chat.ChatSingleScreen
import com.haperezs.culturalfriends.chat.ChatViewModel
import com.haperezs.culturalfriends.finder.FinderScreen
import com.haperezs.culturalfriends.finder.FinderViewModel
import com.haperezs.culturalfriends.translate.TranslateScreen
import com.haperezs.culturalfriends.translate.TranslateViewModel

class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val badge: Boolean,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppComposable() {
    val authViewModel = viewModel<AuthViewModel>()
    val chatViewModel = viewModel<ChatViewModel>()
    val finderViewModel = viewModel<FinderViewModel>()
    val translateViewModel = viewModel<TranslateViewModel>()

    val currentChat by chatViewModel.currentChat.collectAsStateWithLifecycle()
    val user by authViewModel.authState.collectAsStateWithLifecycle()

    val items = listOf(
        BottomNavigationItem(
            title = "Chats",
            icon = Icons.Outlined.ChatBubbleOutline,
            badge = false,
            route = Screen.ChatScreen.route
        ),
        BottomNavigationItem(
            title = "Finder",
            icon = Icons.Outlined.Map,
            badge = false,
            route = Screen.FinderScreen.route
        ),
        BottomNavigationItem(
            title = "Translate",
            icon = Icons.Outlined.Translate,
            badge = false,
            route = Screen.TranslateScreen.route
        ),
    )
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // Routes where back arrow is shown
    val backArrowRoutes = listOf(Screen.ChatSingleScreen.route, Screen.SettingsScreen.route)
    val canNavigateBack = currentRoute in backArrowRoutes

    val title = when (currentRoute) {
        Screen.AuthScreen.route -> "Cultural Friends"
        Screen.ChatScreen.route -> "Chats"
        Screen.ChatSingleScreen.route -> currentChat
        Screen.FinderScreen.route -> "Finder"
        Screen.SettingsScreen.route -> "Settings"
        Screen.TranslateScreen.route -> "Translate"
        else -> "Cultural Friends"
    }

    // If user is not logged in, navigate to Auth Screen
    val startDestination = if (user != null) Screen.FinderScreen.route else Screen.AuthScreen.route

    Scaffold(
        topBar = {
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
        },
        bottomBar = {
            if (currentRoute != Screen.AuthScreen.route) {
                NavigationBar {
                    items.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route){
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                }

                            },
                            label = {
                                Text(text = item.title)
                            },
                            alwaysShowLabel = true,
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (item.badge) {
                                            Badge()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.title
                                    )
                                }
                            }
                        )
                    }
                }
            }
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.AuthScreen.route) { AuthScreen(navController) }
                composable(Screen.ChatScreen.route) {
                    ChatScreen(
                        navController = navController,
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
                        chatViewModel = chatViewModel,
                        translateViewModel = translateViewModel,
                        chatId = chatId!!
                    )
                }
                composable(Screen.FinderScreen.route) {
                    FinderScreen(
                        chatViewModel = chatViewModel,
                        translateViewModel = translateViewModel
                    )
                }
                composable(Screen.SettingsScreen.route) { SettingsScreen(navController) }
                composable(Screen.TranslateScreen.route) {
                    TranslateScreen(
                        translateViewModel = translateViewModel
                    )
                }
            }
        }
    )
}