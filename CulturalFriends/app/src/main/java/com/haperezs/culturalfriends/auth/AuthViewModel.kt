package com.haperezs.culturalfriends.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    // Used to validate if a user is logged in
    private val _authState = MutableStateFlow(auth.currentUser)
    val authState: StateFlow<FirebaseUser?> = _authState

    // Used to display the name of the user logged in
    private val _displayName = MutableStateFlow(auth.currentUser?.displayName ?: "")
    val displayName: StateFlow<String> = _displayName

    // Used to get the uid of the user logged in
    private val _userId = MutableStateFlow(auth.currentUser?.uid ?: "")
    val userId: StateFlow<String> = _userId

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {
        checkIfLoggedIn()
    }

    private fun checkIfLoggedIn(){
        if(auth.currentUser != null){
            _isLoggedIn.value = true
        }
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        if (email.isBlank()){
            Log.d(javaClass.simpleName, "No email provided")
            return
        }
        if (password.isBlank()){
            Log.d(javaClass.simpleName, "No password provided")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = auth.currentUser
                    _isLoggedIn.value = true
                    _displayName.value = auth.currentUser?.displayName ?: ""
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun register(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        if (email.isBlank()){
            Log.d(javaClass.simpleName, "No email provided")
            return
        }
        if (password.isBlank()){
            Log.d(javaClass.simpleName, "No password provided")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = auth.currentUser
                    _isLoggedIn.value = true
                    // Set the email as the display name on registration
                    _displayName.value = email.substringBefore('@')
                    auth.currentUser?.updateProfile(
                        userProfileChangeRequest {
                            displayName = email.substringBefore('@')
                        }
                    )
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun logout() {
        auth.signOut()
        _authState.value = null
        _isLoggedIn.value = false
    }

    fun updateDisplayName(newDisplayName: String){
        val updates = UserProfileChangeRequest.Builder()
            .setDisplayName(newDisplayName)
            .build()

        _authState.value?.updateProfile(updates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(javaClass.simpleName, "User displayName updated to $newDisplayName")
                    _displayName.value = newDisplayName
                } else {
                    Log.e(javaClass.simpleName, "Failed to update display name. ${task.exception}")
                }
            }
    }
}