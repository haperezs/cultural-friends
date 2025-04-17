package com.haperezs.culturalfriends.chat

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.haperezs.culturalfriends.finder.PeopleMarker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = Firebase.auth

    init {
        observeAuthChanges()
    }

    private val _chats = MutableStateFlow<List<Chat?>>(emptyList())
    val chats: StateFlow<List<Chat?>> = _chats

    // To display the correct name of the person you are chatting with in the topbar
    private val _currentChat = MutableStateFlow<String?>(null)
    val currentChat: StateFlow<String?> = _currentChat

    private val _messages = MutableStateFlow<List<Message?>>(emptyList())
    val messages: StateFlow<List<Message?>> = _messages

    // Observe auth status to reload chats if the user logs out
    private fun observeAuthChanges() {
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                fetchChats()
            } else {
                _chats.value = emptyList()
            }
        }
    }

    fun updateCurrentChat(newChat: String) {
        _currentChat.value = newChat
        Log.d(javaClass.simpleName, "Updated currentChat to ${currentChat.value}")
    }

    private fun fetchChats() {
        auth.currentUser?.let { user ->
            db.collection("chats")
                .whereArrayContains("users", user.uid)
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { documents, e ->
                    if (e != null || documents == null) {
                        Log.d(javaClass.simpleName, "Error fetching user chats. $e")
                        return@addSnapshotListener
                    }
                    Log.d(javaClass.simpleName, "Success fetching user chats")
                    _chats.value = emptyList()
                    for (doc in documents) {
                        val chat = doc.toObject(Chat::class.java).copy(id = doc.id)
                        val otherUserId = chat.users.firstOrNull { it != auth.currentUser!!.uid }

                        if (otherUserId != null) {
                            fetchOtherUserName(otherUserId) { otherUserName ->
                                if (otherUserName != null) {
                                    chat.otherUserName = otherUserName

                                    val updatedList = _chats.value.toMutableList()
                                    val index = updatedList.indexOfFirst { it?.id == chat.id }

                                    if (index != -1) {
                                        updatedList[index] = chat
                                    } else {
                                        updatedList.add(chat)
                                    }
                                    _chats.value = updatedList
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun fetchOtherUserName(otherUserId: String, onResult: (String?) -> Unit) {
        db.collection("people")
            .whereEqualTo("uid", otherUserId)
            .limit(1)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.d(javaClass.simpleName, "Error fetching other user display name. $e")
                    return@addSnapshotListener
                }
                if (querySnapshot == null || querySnapshot.size() == 0) {
                    Log.d(javaClass.simpleName, "No public marker for other user found.")
                    return@addSnapshotListener
                }
                val document = querySnapshot.documents[0]
                Log.d(javaClass.simpleName, "Success fetching other user display name: ${document.getString("name")}")
                onResult(document.getString("name"))
            }
    }

    fun fetchMessages(chatId: String) {
        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { documents, e ->
                if (e != null || documents == null) {
                    Log.d(javaClass.simpleName, "Error fetching chat messages. $e")
                    return@addSnapshotListener
                }
                Log.d(javaClass.simpleName, "Success fetching chat messages")
                _messages.value = emptyList()
                for (doc in documents) {
                    val message = doc.toObject(Message::class.java).copy(id = doc.id)
                    _messages.value += message
                }
            }
    }

    fun sendMessage(chatId: String, message: String){
        Log.d(javaClass.simpleName, "Sending message.")
        val chatUpdates = mapOf(
            "lastMessage" to message,
            "lastMessageTimestamp" to Timestamp.now(),
            "lastMessageBy" to auth.currentUser!!.uid,
        )

        val messageUpdates = mapOf(
            "text" to message,
            "sender" to auth.currentUser!!.uid,
            "timestamp" to Timestamp.now(),
        )

        db.collection("chats")
            .document(chatId)
            .update(chatUpdates)
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "Updated last message info for chat $chatId")
            }
            .addOnFailureListener { e ->
                Log.w(javaClass.simpleName, "Error updating last message info for chat $chatId. $e")
            }

        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .add(messageUpdates)
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "Added message to chat $chatId")
            }
            .addOnFailureListener { e ->
                Log.w(javaClass.simpleName, "Error adding message to chat $chatId. $e")
            }
    }
}