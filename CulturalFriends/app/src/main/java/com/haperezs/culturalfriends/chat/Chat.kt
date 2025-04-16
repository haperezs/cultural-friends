package com.haperezs.culturalfriends.chat

import com.google.firebase.Timestamp

data class Chat(
    val id: String = "",
    val lastMessage: String = "",
    val lastMessageTimestamp: Timestamp = Timestamp.now(),
    val users: List<String> = emptyList(),
    val messages: List<Message?> = emptyList()
) { }

data class Message(
    val id: String = "",
    val message: String = "",
    val sender: String = "",
    val timestamp: Timestamp = Timestamp.now()
) { }