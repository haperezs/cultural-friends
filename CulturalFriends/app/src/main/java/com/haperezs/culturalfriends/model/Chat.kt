package com.haperezs.culturalfriends.model

import com.google.firebase.Timestamp

data class Chat(
    val id: String = "",
    val lastMessage: String = "",
    val lastMessageTimestamp: Timestamp = Timestamp.now(),
    val lastMessageBy: String = "",
    val users: List<String> = emptyList(),
    val messages: List<Message?> = emptyList(),
    // Not part of the model in the db, but fetched in ChatViewModel
    var otherUserName: String = ""
) { }