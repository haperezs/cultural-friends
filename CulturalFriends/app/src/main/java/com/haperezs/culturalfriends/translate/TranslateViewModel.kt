package com.haperezs.culturalfriends.translate

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TranslateViewModel : ViewModel() {
    private var repository: TranslateRepository = TranslateRepository()

    private val _inputText = MutableStateFlow<String?>(null)
    val inputText: StateFlow<String?> = _inputText

    private val _outputText = MutableStateFlow<String?>(null)
    val outputText: StateFlow<String?> = _outputText

    private val _sourceLang = MutableStateFlow<String?>("Spanish")
    val sourceLang: StateFlow<String?> = _sourceLang

    private val _targetLang = MutableStateFlow<String?>("English")
    val targetLang: StateFlow<String?> = _targetLang

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun changeInputText(text: String?) {
        _inputText.value = text
    }

    fun translateText() {
        viewModelScope.launch {
            _isLoading.value = true
            val input = _inputText.value ?: ""
            val source = _sourceLang.value
            val target = _targetLang.value ?: "English"
            val result = repository!!.translateText(input, target, source)
            _isLoading.value = false

            result.onSuccess {
                _outputText.value = it
            }.onFailure {
                Log.d(javaClass.simpleName, "Failed to get translation.")
            }
        }
    }
}