package com.haperezs.culturalfriends.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _authState = mutableStateOf<FirebaseUser?>(auth.currentUser)
    val authState = _authState

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
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun logout() {
        auth.signOut()
        _authState.value = null
    }
}