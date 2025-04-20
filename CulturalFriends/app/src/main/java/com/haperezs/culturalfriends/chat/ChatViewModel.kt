package com.haperezs.culturalfriends.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.haperezs.culturalfriends.model.Chat
import com.haperezs.culturalfriends.model.ChatRequest
import com.haperezs.culturalfriends.model.Language
import com.haperezs.culturalfriends.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = Firebase.auth

    init {
        observeAuthChanges()
        fetchUserLanguage()
    }

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats

    private val _chatRequests = MutableStateFlow<List<ChatRequest>>(emptyList())
    val chatRequests: StateFlow<List<ChatRequest>> = _chatRequests

    // To display the correct name of the person you are chatting with in the topbar
    private val _currentChat = MutableStateFlow(Chat())
    val currentChat: StateFlow<Chat> = _currentChat

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    // Observe auth status to reload chats if the user logs out
    private fun observeAuthChanges() {
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                fetchChats()
                fetchChatRequests()
            } else {
                _chats.value = emptyList()
                _chatRequests.value = emptyList()
            }
        }
    }

    fun updateCurrentChat(newChat: Chat) {
        _currentChat.value = newChat
    }

    private fun fetchChats() {
        auth.currentUser?.let { user ->
            db.collection("chats")
                .whereArrayContains("users", user.uid)
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { documents, e ->
                    if (e != null || documents == null) {
                        Log.e(javaClass.simpleName, "Error fetching user chats. $e")
                        return@addSnapshotListener
                    }
                    Log.d(javaClass.simpleName, "Success fetching user chats")
                    _chats.value = emptyList()
                    for (doc in documents) {
                        val chat = doc.toObject(Chat::class.java).copy(id = doc.id)
                        val otherUserId = chat.users.firstOrNull { it != auth.currentUser!!.uid }

                        if (otherUserId != null) {
                            fetchOtherUserInfo(otherUserId) { otherUserName, otherUserLanguage ->
                                if (otherUserName != null && otherUserLanguage != null) {
                                    chat.otherUserName = otherUserName
                                    chat.otherUserLanguage = otherUserLanguage

                                    val updatedList = _chats.value.toMutableList()
                                    val index = updatedList.indexOfFirst { it.id == chat.id }

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

    private fun fetchChatRequests() {
        auth.currentUser?.let { user ->
            db.collection("chatRequests")
                .whereEqualTo("to", user.uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { documents, e ->
                    if (e != null || documents == null) {
                        Log.e(javaClass.simpleName, "Error fetching user chat requests. $e")
                        return@addSnapshotListener
                    }
                    Log.d(javaClass.simpleName, "Success fetching user chat requests")
                    _chatRequests.value = emptyList()
                    for (doc in documents) {
                        val chatRequest = doc.toObject(ChatRequest::class.java).copy(id = doc.id)
                        val otherUserId = chatRequest.from

                        fetchOtherUserInfo(otherUserId) { otherUserName, _ ->
                            if (otherUserName != null) {
                                chatRequest.otherUserName = otherUserName

                                val updatedList = _chatRequests.value.toMutableList()
                                val index = updatedList.indexOfFirst { it.id == chatRequest.id }

                                if (index != -1) {
                                    updatedList[index] = chatRequest
                                } else {
                                    updatedList.add(chatRequest)
                                }
                                _chatRequests.value = updatedList
                            }
                        }
                    }
                }
        }
    }

    fun sendChatRequest(otherUserId: String){
        val updates = mapOf(
            "to" to otherUserId,
            "from" to auth.currentUser!!.uid,
            "timestamp" to Timestamp.now(),
        )

        db.collection("chatRequests")
            .add(updates)
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "Created new chat request.")
            }
            .addOnFailureListener { e ->
                Log.e(javaClass.simpleName, "Error creating new chat request. $e")
            }
    }

    // Create a new chat between both users and remove the chatRequest
    fun acceptChatRequest(chatRequest: ChatRequest){
        val updates = mapOf(
            "lastMessageTimestamp" to Timestamp.now(),
            "users" to FieldValue.arrayUnion(chatRequest.to, chatRequest.from)
        )

        db.collection("chats")
            .add(updates)
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "Accepted request and created new chat.")
            }
            .addOnFailureListener { e ->
                Log.e(javaClass.simpleName, "Error accepting request and creating new chat. $e")
            }

        db.collection("chatRequests")
            .document(chatRequest.id)
            .delete()
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "Accepted request and deleted chat request.")
            }
            .addOnFailureListener { e ->
                Log.e(javaClass.simpleName, "Error accepting request and deleting chat request. $e")
            }
    }

    fun denyChatRequest(chatRequest: ChatRequest){
        db.collection("chatRequests")
            .document(chatRequest.id)
            .delete()
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "Denied and deleted chat request.")
            }
            .addOnFailureListener { e ->
                Log.e(javaClass.simpleName, "Error denying and deleting chat request. $e")
            }
    }

    private fun fetchOtherUserInfo(otherUserId: String, onResult: (String?, String?) -> Unit) {
        db.collection("people")
            .whereEqualTo("uid", otherUserId)
            .limit(1)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.d(javaClass.simpleName, "Error fetching other user info. $e")
                    return@addSnapshotListener
                }
                if (querySnapshot == null || querySnapshot.size() == 0) {
                    Log.d(javaClass.simpleName, "No public marker for other user found.")
                    return@addSnapshotListener
                }
                val document = querySnapshot.documents[0]
                onResult(document.getString("name"), document.getString("language"))
            }
    }

    fun fetchMessages(chatId: String) {
        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { documents, e ->
                if (e != null || documents == null) {
                    Log.e(javaClass.simpleName, "Error fetching chat messages. $e")
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
                Log.e(javaClass.simpleName, "Error updating last message info for chat $chatId. $e")
            }

        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .add(messageUpdates)
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "Added message to chat $chatId")
            }
            .addOnFailureListener { e ->
                Log.e(javaClass.simpleName, "Error adding message to chat $chatId. $e")
            }
    }

    fun fetchUserLanguage(){

    }
}