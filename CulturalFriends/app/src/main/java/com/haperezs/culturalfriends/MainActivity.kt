package com.haperezs.culturalfriends

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.haperezs.culturalfriends.chat.ChatScreen
import com.haperezs.culturalfriends.finder.FinderScreen
import com.haperezs.culturalfriends.translate.TranslateScreen
import com.haperezs.culturalfriends.ui.theme.CulturalFriendsTheme

class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val badge: Boolean,
    val route: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CulturalFriendsTheme {
                val items = listOf(
                    BottomNavigationItem(
                        title = "Chat",
                        icon = Icons.Outlined.ChatBubbleOutline,
                        badge = true,
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

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                items.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = false,
                                        onClick = {
                                            Log.d(javaClass.simpleName,"Navigate to ${item.title}")
                                            navController.navigate(item.route)
                                        },
                                        label = {
                                            Text(text = item.title)
                                        },
                                        alwaysShowLabel = true,
                                        icon = {
                                            BadgedBox(
                                                badge = {
                                                    if(item.badge) {
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
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination  = Screen.FinderScreen.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(Screen.ChatScreen.route) { ChatScreen(navController) }
                            composable(Screen.FinderScreen.route) { FinderScreen(navController) }
                            composable(Screen.TranslateScreen.route) { TranslateScreen(navController) }
                        }
                    }
                }
            }
        }
    }
}
