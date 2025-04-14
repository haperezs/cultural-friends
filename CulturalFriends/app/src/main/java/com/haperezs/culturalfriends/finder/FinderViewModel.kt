package com.haperezs.culturalfriends.finder

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class PeopleMarker(
    val id: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val name: String = "",
    val uid: String = ""
) {
    fun toLatLng(): LatLng = LatLng(latitude, longitude)
}

class FinderViewModel : ViewModel() {
    init {
        fetchPeopleMarkers()
    }

    // This marker is shows where the public marker will be set if the user clicks the share button,
    // currently is only visible to the same user
    private val _previewMarker = MutableStateFlow<LatLng?>(null)
    val previewMarker: StateFlow<LatLng?> = _previewMarker

    private val _publicMarker = MutableStateFlow<LatLng?>(null)
    val publicMarker: StateFlow<LatLng?> = _publicMarker

    // These are read from the db and updated by other people
    private val _peopleMarkers = MutableStateFlow<List<PeopleMarker?>>(emptyList())
    val peopleMarkers: StateFlow<List<PeopleMarker?>> = _peopleMarkers

    fun updatePreviewMarker(position: LatLng?) {
        _previewMarker.value = position
    }

    fun updatePublicMarker(position: LatLng?) {
        _publicMarker.value = position
    }

    private fun fetchPeopleMarkers() {
        FirebaseFirestore.getInstance()
            .collection("people")
            .addSnapshotListener { documents, e ->
                if (e != null || documents == null) {
                    Log.d(javaClass.simpleName, "Error fetching people markers. $e")
                    return@addSnapshotListener
                }
                Log.d(javaClass.simpleName, "Success fetching people markers")
                _peopleMarkers.value = emptyList()
                for (doc in documents) {
                    val marker = doc.toObject(PeopleMarker::class.java).copy(id = doc.id)
                    _peopleMarkers.value += marker
                }
            }
    }
}