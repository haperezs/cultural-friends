package com.haperezs.culturalfriends.finder

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = Firebase.auth

    init {
        fetchPeopleMarkers()
        fetchPublicMarker()
    }

    // This marker is shows where the public marker will be set if the user clicks the share button,
    // currently is only visible to the same user
    private val _previewMarker = MutableStateFlow<LatLng?>(null)
    val previewMarker: StateFlow<LatLng?> = _previewMarker

    private val _publicMarkerId = MutableStateFlow<String?>(null)
    private val _publicMarker = MutableStateFlow<PeopleMarker?>(null)
    val publicMarker: StateFlow<PeopleMarker?> = _publicMarker

    // These are read from the db and updated by other people
    private val _peopleMarkers = MutableStateFlow<List<PeopleMarker?>>(emptyList())
    val peopleMarkers: StateFlow<List<PeopleMarker?>> = _peopleMarkers

    fun updatePreviewMarker(position: LatLng?) {
        _previewMarker.value = position
    }

    // Fetch the users own marker to have a document (id) to push updates to,
    // if it does not exist it will be created on the update operation
    private fun fetchPublicMarker() {
        val userId = auth.currentUser?.uid
        db.collection("people")
            .whereEqualTo("uid", userId)
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    _publicMarkerId.value = document.id
                    _publicMarker.value = document.toObject(PeopleMarker::class.java)?.copy(id = document.id)
                    Log.d(javaClass.simpleName, "Success fetching user public marker. id: ${_publicMarkerId.value}")
                } else {
                    Log.d(javaClass.simpleName, "No public marker for user found.")
                }
            }
            .addOnFailureListener { e ->
                Log.d(javaClass.simpleName, "Error fetching user public marker. $e")
            }
    }

    fun updatePublicMarker(position: LatLng) {
        Log.d(javaClass.simpleName, "Updating public marker.")
        val updates = mapOf(
            "latitude" to position.latitude,
            "longitude" to position.longitude,
            "name" to auth.currentUser?.displayName,
            "uid" to auth.currentUser?.uid
        )

        if (_publicMarkerId.value?.isNotBlank() == true) {
            db.collection("people")
                .document(_publicMarkerId.value!!)
                .set(updates, SetOptions.merge())
        } else {
            db.collection("people")
                .add(updates)
                .addOnSuccessListener { documentReference ->
                    val docId = documentReference.id
                    Log.d(javaClass.simpleName, "Public marker added with Id: $docId")
                    _publicMarkerId.value = docId
                }
                .addOnFailureListener { e ->
                    Log.w(javaClass.simpleName, "Error creating public marker. $e")
                }
        }

    }

    private fun fetchPeopleMarkers() {
        db.collection("people")
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