package com.haperezs.culturalfriends.translate

import com.haperezs.culturalfriends.model.Language
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslateApi {
    @GET("language/translate/v2")
    suspend fun translate(
        @Query("q") text: String,
        @Query("source") sourceLang: String,
        @Query("target") targetLang: String,
        @Query("key") apiKey: String
    ): TranslationResponse

    @GET("language/translate/v2/languages")
    suspend fun getLanguages(
        @Query("key") apiKey: String,
        @Query("target") targetLanguage: String = "en"
    ): LanguagesResponse

    data class TranslationResponse(
        val data: Data
    ){
        data class Data(
            val translations: List<Translation>
        ) {
            data class Translation(
                val translatedText: String
            )
        }
    }

    data class LanguagesResponse(
        val data: LanguagesData
    ) {
        data class LanguagesData(
            val languages: List<Language>
        )
    }
}