package com.haperezs.culturalfriends

sealed class Screen(val route: String) {
    data object AuthScreen: Screen("auth")
    data object ChatScreen: Screen("chat")
    data object FinderScreen: Screen("finder")
    data object TranslateScreen: Screen("translate")
    data object SettingsScreen: Screen("settings")
}