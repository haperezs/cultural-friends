package com.haperezs.culturalfriends.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.haperezs.culturalfriends.finder.PeopleMarker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = Firebase.auth

    init {
        fetchChats()
    }

    private val _chats = MutableStateFlow<List<Chat?>>(emptyList())
    val chats: StateFlow<List<Chat?>> = _chats

    private fun fetchChats() {
        auth.currentUser?.let {
            db.collection("chats")
                .whereArrayContains("users", it.uid)
                .addSnapshotListener { documents, e ->
                    if (e != null || documents == null) {
                        Log.d(javaClass.simpleName, "Error fetching user chats. $e")
                        return@addSnapshotListener
                    }
                    Log.d(javaClass.simpleName, "Success fetching user chats")
                    _chats.value = emptyList()
                    for (doc in documents) {
                        val chat = doc.toObject(Chat::class.java).copy(id = doc.id)
                        Log.d(javaClass.simpleName, "Found chat: $chat")
                        _chats.value += chat
                    }
                }
        }
    }
}