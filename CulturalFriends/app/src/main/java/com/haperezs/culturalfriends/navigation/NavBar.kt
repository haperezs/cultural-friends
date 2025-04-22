package com.haperezs.culturalfriends.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.haperezs.culturalfriends.BottomNavigationItem

@Composable
fun NavBar(
    navController: NavController,
    currentRoute: String
) {
    // Each item is a tab
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