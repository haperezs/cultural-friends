package com.haperezs.culturalfriends.translate

import android.util.Log
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class TranslateRequest(
    val text: String,
    val target: String,
    val source: String? = null
)

data class TranslateResponse(
    @SerializedName("translatedText")
    val translatedText: String
)

interface TranslateApi {
    @POST("translateText")
    suspend fun translate(
        @Body request: TranslateRequest
    ): Response<TranslateResponse>
}

class TranslateRepository() {
    private val translateApi: TranslateApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://translatetext-t3xbcm7qqa-uc.a.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslateApi::class.java)
    }

    suspend fun translateText(text: String, target: String, source: String? = null): Result<String> {
        Log.d(javaClass.simpleName, "Started translate")
        return try {
            val response = translateApi.translate(
                TranslateRequest(text = text, target = target, source = source)
            )

            if (response.isSuccessful) {
                Log.d(javaClass.simpleName, "Success translate")
                val translation = response.body()?.translatedText
                if (translation != null) {
                    Result.success(translation)
                } else {
                    Result.failure(Exception("Empty translation"))
                }
            } else {
                Log.d(javaClass.simpleName, "Error translate")
                val error = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("API Error: $error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}