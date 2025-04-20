package com.haperezs.culturalfriends.finder.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState

@Composable
fun PreviewMarker(
    position: LatLng
) {
    MarkerComposable(
        state = MarkerState(position = position),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Preview marker icon",
                    tint = Color(0xFF1565C0),
                    modifier = Modifier.matchParentSize()
                )
            }
        }
    }
}