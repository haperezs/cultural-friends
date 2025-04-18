package com.haperezs.culturalfriends.model

import com.google.firebase.Timestamp

data class Message(
    val id: String = "",
    val text: String = "",
    val sender: String = "",
    val timestamp: Timestamp = Timestamp.now()
) { }