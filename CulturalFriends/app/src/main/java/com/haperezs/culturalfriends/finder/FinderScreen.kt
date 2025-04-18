package com.haperezs.culturalfriends.finder

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.haperezs.culturalfriends.Screen
import com.haperezs.culturalfriends.auth.AuthViewModel
import com.haperezs.culturalfriends.chat.ChatViewModel
import com.haperezs.culturalfriends.model.PeopleMarker

@Composable
fun FinderScreen(
    navController: NavController,
    chatViewModel: ChatViewModel,
    finderViewModel: FinderViewModel = viewModel()
) {
    val iconColor = Color(0xFF1565C0)
    val defaultInfo = PeopleMarker("0", 0.0,0.0,"Placeholder", "0")
    val defaultTarget = LatLng(47.607616036871896, -122.31669998841178)

    val previewMarker by finderViewModel.previewMarker.collectAsStateWithLifecycle()
    val publicMarker by finderViewModel.publicMarker.collectAsStateWithLifecycle()
    val peopleMarkers by finderViewModel.peopleMarkers.collectAsStateWithLifecycle()

    var showInfo by remember { mutableStateOf(false) }
    var selectedMarkerInfo by remember { mutableStateOf(defaultInfo) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultTarget, 12f)
    }

    // Centers the map to the position of the user's pin, only if it exists and is loaded successfully
    LaunchedEffect(publicMarker) {
        if (publicMarker != null && !finderViewModel.publicMarkerCentered) {
            finderViewModel.publicMarkerCentered = true

            val target = LatLng(publicMarker!!.latitude, publicMarker!!.longitude)
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(target, 14f),
                durationMs = 1000
            )

        }
    }

    // Hides the pin info window when the map is scrolled
    LaunchedEffect(cameraPositionState.isMoving) {
        showInfo = false
    }

    Column {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            GoogleMap(
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false
                ),
                onMapClick = { position ->
                    finderViewModel.updatePreviewMarker(position)
                    showInfo = false
                },
                modifier = Modifier.fillMaxSize(),
            ){
                previewMarker?.let { position ->
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
                                    tint = iconColor,
                                    modifier = Modifier.matchParentSize()
                                )
                            }
                        }
                    }
                }

                peopleMarkers.forEach{marker ->
                    if (marker != null) {
                        key(marker.id + marker.name){
                            MarkerComposable(
                                state = MarkerState(position = marker.toLatLng()),
                                onClick = {
                                    selectedMarkerInfo = peopleMarkers.find { it?.id == marker.id }!!
                                    showInfo = true
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
                                            tint = iconColor,
                                            modifier = Modifier.matchParentSize()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (previewMarker != null) {
                FloatingActionButton(
                    onClick = {
                        finderViewModel.publicMarkerCentered = false
                        finderViewModel.updatePublicMarker(previewMarker!!)
                        finderViewModel.updatePreviewMarker(null)
                    },
                    shape = CircleShape,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PersonAddAlt1,
                        contentDescription = "Add public marker button"
                    )
                }
            }
            if (showInfo) {
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
                                text = selectedMarkerInfo.name,
                                modifier = Modifier
                                            .padding(4.dp, 0.dp),
                                color = Color.White,
                            )
                        }
                        Row (
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    chatViewModel.sendChatRequest(selectedMarkerInfo.uid)
                                    selectedMarkerInfo.canSendRequest = false
                                    selectedMarkerInfo.chatRequestButtonText = "Request sent"
                                    Log.d(javaClass.simpleName, selectedMarkerInfo.toString())
                                },
                                enabled = selectedMarkerInfo.canSendRequest
                            ) {
                                Text(
                                    text = selectedMarkerInfo.chatRequestButtonText,
                                    modifier = Modifier
                                        .padding(4.dp, 0.dp),
                                    color = Color.White,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}