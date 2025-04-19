package com.haperezs.culturalfriends.translate

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import com.haperezs.culturalfriends.BuildConfig
import com.haperezs.culturalfriends.model.Language
import retrofit2.HttpException
import java.io.IOException

class TranslateRepository() {
    private val apiKey = BuildConfig.TRANSLATE_API_KEY

    private val api: TranslateApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://translation.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslateApi::class.java)
    }

    suspend fun translateText(text: String, sourceLange: String, targetLang: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.translate(text, sourceLange, targetLang, apiKey)
                response.data.translations.firstOrNull()?.translatedText ?: "Translation failed"
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("translateText()", "HTTP ${e.code()} error: $errorBody")
                "HTTP error ${e.code()}"
            } catch (e: IOException) {
                Log.e("translateText()", "Network error: ${e.message}")
                "Network error"
            } catch (e: Exception) {
                Log.e("translateText()", "Unknown error: ${e.message}")
                "Unexpected error"
            }
        }
    }

    suspend fun fetchSupportedLanguages(): List<Language> {
        val response = api.getLanguages(apiKey)
        return response.data.languages
    }
}