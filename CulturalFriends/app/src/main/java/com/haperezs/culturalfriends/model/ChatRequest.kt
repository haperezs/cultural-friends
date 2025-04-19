package com.haperezs.culturalfriends.model

import com.google.firebase.Timestamp

data class ChatRequest(
    val id: String = "",
    val to: String = "",
    val from: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    // Not part of the model in the db, but fetched in ChatViewModel
    var otherUserName: String = ""
)