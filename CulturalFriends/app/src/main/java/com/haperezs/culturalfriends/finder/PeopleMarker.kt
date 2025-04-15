package com.haperezs.culturalfriends.finder

import com.google.android.gms.maps.model.LatLng

data class PeopleMarker(
    val id: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val name: String = "",
    val uid: String = ""
) {
    fun toLatLng(): LatLng = LatLng(latitude, longitude)
}