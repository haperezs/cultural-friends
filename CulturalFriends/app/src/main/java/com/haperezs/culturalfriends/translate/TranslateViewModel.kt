package com.haperezs.culturalfriends.translate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haperezs.culturalfriends.model.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TranslateViewModel : ViewModel() {
    private var repository: TranslateRepository = TranslateRepository()

    init {
        fetchSupportedLanguages()
    }
    private val _languages = MutableStateFlow<List<Language>>(emptyList())
    val languages: StateFlow<List<Language>> = _languages

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText

    private val _outputText = MutableStateFlow("")
    val outputText: StateFlow<String> = _outputText

    private val _sourceLang = MutableStateFlow(Language("es", "Spanish"))
    val sourceLang: StateFlow<Language> = _sourceLang

    private val _targetLang = MutableStateFlow(Language("en", "English"))
    val targetLang: StateFlow<Language> = _targetLang

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private fun fetchSupportedLanguages() {
        viewModelScope.launch {
            val result = repository.fetchSupportedLanguages()
            _languages.value = result
        }
    }

    fun changeInputText(text: String) {
        _inputText.value = text
    }

    fun translateText() {
        viewModelScope.launch {
            _isLoading.value = true
            val input = _inputText.value
            val source = _sourceLang.value.language
            val target = _targetLang.value.language
            val result = repository.translateText(input, source, target)
            _isLoading.value = false
            _outputText.value = result
        }
    }

    fun updateSourceLanguage(language: Language){
        _sourceLang.value = language
    }

    fun updateTargetLanguage(language: Language){
        _targetLang.value = language
    }

    fun swapSourceAndTargetLanguage(){
        _sourceLang.value = _targetLang.value.also { _targetLang.value = _sourceLang.value }
        _inputText.value = _outputText.value.also { _outputText.value = _inputText.value }
    }
}