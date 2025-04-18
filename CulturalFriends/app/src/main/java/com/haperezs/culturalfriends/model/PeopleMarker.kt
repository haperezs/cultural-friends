package com.haperezs.culturalfriends.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.android.gms.maps.model.LatLng

data class PeopleMarker(
    val id: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val name: String = "",
    val uid: String = "",
) {
    var canSendRequest: Boolean by mutableStateOf(true)
    var chatRequestButtonText: String by mutableStateOf("Start chat")

    fun toLatLng(): LatLng = LatLng(latitude, longitude)
}