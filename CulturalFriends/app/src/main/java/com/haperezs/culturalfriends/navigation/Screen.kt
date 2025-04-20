package com.haperezs.culturalfriends.navigation

sealed class Screen(
    val route: String,
) {
    data object AuthScreen: Screen("auth")
    data object ChatScreen: Screen("chat")
    data object ChatSingleScreen: Screen("chatSingle/{chatId}") {
        fun createRoute(chatId: String) = "chatSingle/$chatId"
    }
    data object FinderScreen: Screen("finder")
    data object SettingsScreen: Screen("settings")
    data object TranslateScreen: Screen("translate")
}