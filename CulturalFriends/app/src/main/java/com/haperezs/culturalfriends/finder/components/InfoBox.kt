package com.haperezs.culturalfriends.finder.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.haperezs.culturalfriends.model.Language
import com.haperezs.culturalfriends.model.PeopleMarker

@Composable
fun InfoBox(
    markerInfo: PeopleMarker,
    languages: List<Language>,
    onClickSendMessage: () -> Unit,
){
    val languageName = languages.firstOrNull { it.language == markerInfo.language }?.name ?: "Not specified"

    Box (
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    Color.Black.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(8.dp)
                )
                .fillMaxWidth()
                .padding(16.dp, 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(0.dp, 0.dp, 0.dp, 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "User info window icon",
                    tint = Color.White
                )
                Text(
                    text = markerInfo.name,
                    modifier = Modifier.padding(4.dp, 0.dp),
                    color = Color.White,
                )
            }
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(0.dp, 0.dp, 0.dp, 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Language,
                    contentDescription = "User language icon",
                    tint = Color.White
                )
                Text(
                    text = languageName,
                    modifier = Modifier.padding(4.dp, 0.dp),
                    color = Color.White,
                )
            }
            Row (
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                OutlinedButton(
                    onClick = {
                        onClickSendMessage()
                        markerInfo.canSendRequest = false
                        markerInfo.chatRequestButtonText = "Request sent"
                    },
                    enabled = markerInfo.canSendRequest
                ) {
                    Text(
                        text = markerInfo.chatRequestButtonText,
                        modifier = Modifier.padding(4.dp, 0.dp),
                        color = Color.White,
                    )
                }
            }
        }
    }
}