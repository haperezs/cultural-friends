package com.haperezs.culturalfriends.finder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.haperezs.culturalfriends.auth.AuthViewModel

@Composable
fun FinderScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    finderViewModel: FinderViewModel = viewModel()
) {
    val iconColor = Color(0xFF1565C0)
    val defaultTarget = LatLng(47.607616036871896, -122.31669998841178)

    val displayName by authViewModel.displayName.collectAsStateWithLifecycle()
    val previewMarker by finderViewModel.previewMarker.collectAsStateWithLifecycle()
    val publicMarker by finderViewModel.publicMarker.collectAsStateWithLifecycle()
    val peopleMarkers by finderViewModel.peopleMarkers.collectAsStateWithLifecycle()

    var isMapLoaded by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultTarget, 12f)
    }

    Column {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val map = GoogleMap(
                cameraPositionState = cameraPositionState,
                onMapLoaded = {
                    var isMapLoaded = true
                },
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false
                ),
                onMapClick = { position ->
                    finderViewModel.updatePreviewMarker(position)
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

                publicMarker?.let { position ->
                    MarkerComposable(
                        state = MarkerState(position = position),
                    ) {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {
                            Text(
                                text = displayName,
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

                peopleMarkers.forEach{marker ->
                    if (marker != null) {
                        key(marker.id + marker.name){
                            MarkerComposable(
                                state = MarkerState(position = marker.toLatLng()),
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
            FloatingActionButton(
                onClick = {
                    finderViewModel.updatePublicMarker(previewMarker)
                    finderViewModel.updatePreviewMarker(null)
                },
                shape = CircleShape,
                modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
            ) {
                Icon(Icons.Filled.PersonAddAlt1, "Add public marker button")
            }
        }
    }
}