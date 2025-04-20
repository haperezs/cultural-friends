package com.haperezs.culturalfriends.finder.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.haperezs.culturalfriends.model.PeopleMarker

@Composable
fun PublicMarker(
    marker: PeopleMarker,
    onClick: () -> Unit
) {
    key(marker.id + marker.name){
        MarkerComposable(
            state = MarkerState(position = marker.toLatLng()),
            onClick = {
                onClick()
                true
            }
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Text(
                    text = marker.name,
                    color = Color.DarkGray,
                    modifier = Modifier
                        .background(Color.LightGray)
                        .padding(4.dp, 2.dp)
                )
                Box(
                    modifier = Modifier
                        .size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Accessibility,
                        contentDescription = "Public marker icon",
                        tint = Color(0xFF1565C0),
                        modifier = Modifier.matchParentSize()
                    )
                }
            }
        }
    }
}