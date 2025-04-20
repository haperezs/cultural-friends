package com.haperezs.culturalfriends.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.haperezs.culturalfriends.model.Chat
import com.haperezs.culturalfriends.utils.formatTimestamp

@Composable
fun ChatRow (
  chat: Chat,
  userId: String,
  onClick: () -> Unit
) {
    val lastMessageText = if (chat.lastMessageBy == userId){
        "You: ${chat.lastMessage}"
    } else if (chat.lastMessage.isNotBlank()){
        chat.lastMessage
    } else {
        "> Be the first to chat!"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.Gray, shape = CircleShape)
                .padding(6.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Chat row icon",
                tint = Color.White,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = chat.otherUserName,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = lastMessageText,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))


        Text(
            text = formatTimestamp(chat.lastMessageTimestamp),
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
    }
}