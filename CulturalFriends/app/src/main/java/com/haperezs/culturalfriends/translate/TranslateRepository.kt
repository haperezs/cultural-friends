package com.haperezs.culturalfriends.translate

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import com.haperezs.culturalfriends.BuildConfig
import retrofit2.HttpException
import java.io.IOException

data class TranslationResponse(
    val data: Data
)

data class Data(
    val translations: List<Translation>
)

data class Translation(
    val translatedText: String
)

interface TranslationApi {
    @GET("language/translate/v2")
    suspend fun translate(
        @Query("q") text: String,
        @Query("target") targetLang: String,
        @Query("key") apiKey: String
    ): TranslationResponse
}

class TranslateRepository() {
    private val apiKey = BuildConfig.TRANSLATE_API_KEY

    private val api: TranslationApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://translation.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslationApi::class.java)
    }

    suspend fun translateText(text: String, targetLang: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.translate(text, targetLang, apiKey)
                Log.d(javaClass.simpleName, "responso ${response.toString()}")
                response.data.translations.firstOrNull()?.translatedText ?: "Translation failed"
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("TranslateRepository", "HTTP ${e.code()} error: $errorBody")
                "HTTP error ${e.code()}"

            } catch (e: IOException) {
                Log.e("TranslateRepository", "Network error: ${e.message}")
                "Network error"

            } catch (e: Exception) {
                Log.e("TranslateRepository", "Unknown error: ${e.message}")
                "Unexpected error"
            }
        }
    }

}