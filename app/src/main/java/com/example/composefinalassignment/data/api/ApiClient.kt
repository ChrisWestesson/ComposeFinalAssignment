package com.example.composefinalassignment.data.api

import com.example.composefinalassignment.data.models.HoroscopeData
import com.example.composefinalassignment.data.models.HoroscopeResponse
import com.example.composefinalassignment.data.models.Meme
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ApiClient {
    companion object {
        private const val HOROSCOPE_API_URL = "https://horoscope-app-api.vercel.app/api/v1/get-horoscope/daily"
        private const val MEME_API_BASE_URL = "https://api.memegen.link/images/"

        private const val DEFAULT_MEME_TEMPLATE = "aag"
        private const val DEFAULT_MEME_TEXT = "I_cannot_read_you"
        private const val TEMPLATE_URL_POSITION = 4

        fun createMemeImageUrl(template: String, topText: String, bottomText: String): String {
            val safeTopText = topText.replace(" ", "_")
            val safeBottomText = bottomText.replace(" ", "_")
            return "$MEME_API_BASE_URL$template/$safeTopText/$safeBottomText.jpg"
        }

        fun extractTemplateFromUrl(url: String): String {
            return url.split("/").getOrNull(TEMPLATE_URL_POSITION) ?: DEFAULT_MEME_TEMPLATE
        }
    }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
            connectTimeoutMillis = 15000
        }
    }

    suspend fun fetchDailyHoroscope(sign: String): NetworkResult<HoroscopeData> {
        return try {
            val response: HoroscopeResponse = client.get(HOROSCOPE_API_URL) {
                url {
                    parameters.append("sign", sign)
                    parameters.append("day", "TODAY")
                }
            }.body()

            NetworkResult.Success(response.data)
        } catch (e: Exception) {
            NetworkResult.Error(
                message = "Failed to fetch horoscope for $sign",
                cause = e
            )
        }
    }

    suspend fun fetchMemeTemplates(): NetworkResult<List<Meme>> {
        return try {
            val response: List<Meme> = client.get(MEME_API_BASE_URL).body()
            NetworkResult.Success(response)
        } catch (e: Exception) {
            NetworkResult.Error("Failed to fetch meme templates", e)
        }
    }

    fun createMemeUrl(memes: List<Meme>?, sign: String, horoscope: String): String {
        val template = memes?.randomOrNull()?.url?.let { extractTemplateFromUrl(it) } ?: DEFAULT_MEME_TEMPLATE
        val bottomText = horoscope.split(".").lastOrNull { it.isNotBlank() } ?: DEFAULT_MEME_TEXT

        return createMemeImageUrl(template, sign, bottomText)
    }

}