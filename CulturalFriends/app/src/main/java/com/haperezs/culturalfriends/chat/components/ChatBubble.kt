package com.haperezs.culturalfriends.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.haperezs.culturalfriends.model.Message

@Composable
fun ChatBubble(
    message: Message,
    isCurrentUser: Boolean
) {
    Row(
        modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                        .background(
                            color = if (isCurrentUser) Color(0xFFC6D1F8) else Color(0xFFE1E1E1),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(12.dp)
                        .widthIn(max = 250.dp)
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}