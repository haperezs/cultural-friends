package com.haperezs.culturalfriends

sealed class Screen(val route: String) {
    data object ChatScreen: Screen("chat_screen")
    data object FinderScreen: Screen("finder_screen")
    data object TranslateScreen: Screen("translate_screen")
}