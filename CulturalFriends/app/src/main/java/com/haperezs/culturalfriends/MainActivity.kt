package com.haperezs.culturalfriends

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.rememberNavController
import com.haperezs.culturalfriends.ui.theme.CulturalFriendsTheme

class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val badge: Boolean,
)

class MainActivity : ComponentActivity() {
    @OptIn(UnstableApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CulturalFriendsTheme {
                val items = listOf(
                    BottomNavigationItem(
                        title = "Chat",
                        icon = Icons.Filled.ChatBubble,
                        badge = true,
                    ),
                    BottomNavigationItem(
                        title = "Finder",
                        icon = Icons.Filled.Map,
                        badge = false,
                    ),
                    BottomNavigationItem(
                        title = "Translate",
                        icon = Icons.Filled.Translate,
                        badge = false,
                    ),
                )
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
                    ) {

                    }
                }
            }
        }
    }
}


