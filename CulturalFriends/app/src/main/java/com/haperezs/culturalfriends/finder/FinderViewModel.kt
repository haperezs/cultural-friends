package com.haperezs.culturalfriends.finder

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.haperezs.culturalfriends.model.PeopleMarker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
    // Helps to avoid re-centering the map after each tab change
    var publicMarkerCentered = false

    // These are read from the db and updated by other people
    private val _peopleMarkers = MutableStateFlow<List<PeopleMarker>>(emptyList())
    val peopleMarkers: StateFlow<List<PeopleMarker>> = _peopleMarkers

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
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.e(javaClass.simpleName, "Error fetching user public marker. $e")
                    return@addSnapshotListener
                }
                if (querySnapshot == null || querySnapshot.size() == 0) {
                    Log.d(javaClass.simpleName, "No public marker for user found.")
                    return@addSnapshotListener
                }
                val document = querySnapshot.documents[0]
                _publicMarkerId.value = document.id
                _publicMarker.value = document.toObject(PeopleMarker::class.java)?.copy(id = document.id)
                Log.d(javaClass.simpleName, "Success fetching user public marker. id: ${_publicMarkerId.value}")
            }
    }

    fun createPublicMarker() {
        val updates = mapOf(
            "latitude" to 0,
            "longitude" to 0,
            "visible" to false,
            "language" to "en",
            "name" to "New user",
            "uid" to auth.currentUser?.uid
        )

        db.collection("people")
            .add(updates)
            .addOnSuccessListener { documentReference ->
                val docId = documentReference.id
                Log.d(javaClass.simpleName, "Public marker added with Id: $docId")
                _publicMarkerId.value = docId
            }
            .addOnFailureListener { e ->
                Log.e(javaClass.simpleName, "Error creating public marker. $e")
            }
    }

    // Update the position on an existing public marker
    fun updatePublicMarker(position: LatLng) {
        val updates = mapOf(
            "latitude" to position.latitude,
            "longitude" to position.longitude,
            "visible" to true,
            "name" to auth.currentUser?.displayName
        )

        if (_publicMarkerId.value?.isNotBlank() == true) {
            db.collection("people")
                .document(_publicMarkerId.value!!)
                .set(updates, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d(javaClass.simpleName, "Updated public marker name and language")
                }
                .addOnFailureListener { e ->
                    Log.e(javaClass.simpleName, "Error updating public marker name and language. $e")
                }
        }
    }

    // Update the user displayName and language on an existing public marker
    fun updatePublicMarker(newDisplayName: String, newLanguage: String) {
        val updates = mapOf(
            "name" to newDisplayName,
            "language" to newLanguage
        )

        if (_publicMarkerId.value?.isNotBlank() == true) {
            db.collection("people")
                .document(_publicMarkerId.value!!)
                .set(updates, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d(javaClass.simpleName, "Updated public marker name")
                }
                .addOnFailureListener { e ->
                    Log.e(javaClass.simpleName, "Error updating public marker name. $e")
                }
        }
    }

    private fun fetchPeopleMarkers() {
        db.collection("people")
            .whereEqualTo("visible", true)
            .addSnapshotListener { documents, e ->
                if (e != null || documents == null) {
                    Log.e(javaClass.simpleName, "Error fetching people markers. $e")
                    return@addSnapshotListener
                }
                Log.d(javaClass.simpleName, "Success fetching people markers")
                _peopleMarkers.value = emptyList()
                for (doc in documents) {
                    val marker = doc.toObject(PeopleMarker::class.java).copy(id = doc.id)
                    if (marker.uid == auth.currentUser?.uid){
                        // Prevent the user from starting chats with itself
                        marker.canSendRequest = false
                        marker.chatRequestButtonText = "Your marker"
                    }
                    _peopleMarkers.value += marker
                }
            }
    }
}