package com.example.composefinalassignment.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composefinalassignment.data.api.ApiClient
import com.example.composefinalassignment.data.api.NetworkResult
import com.example.composefinalassignment.data.constants.Constants
import com.example.composefinalassignment.data.models.DailyHoroscope
import com.example.composefinalassignment.data.models.HoroscopeData
import com.example.composefinalassignment.data.models.Meme
import com.example.composefinalassignment.data.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.asStateFlow

class HoroscopeViewModel : ViewModel() {
    private val apiClient = ApiClient()

    private val _uiState = MutableStateFlow<UiState<Map<String, DailyHoroscope>>>(UiState.Loading)
    val uiState: StateFlow<UiState<Map<String, DailyHoroscope>>> = _uiState.asStateFlow()

    init {
        fetchAllHoroscopes()
    }

    fun fetchAllHoroscopes() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UiState.Loading
            try {
                val memeTemplates = fetchMemeTemplates()
                val horoscopeResults = fetchZodiacHoroscopes()
                val horoscopesWithMemes = createHoroscopesWithMemes(horoscopeResults, memeTemplates)
                _uiState.value = UiState.Success(horoscopesWithMemes)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to load daily horoscopes: ${e.message}")
            }
        }
    }

    private suspend fun fetchMemeTemplates(): List<Meme>? {
        return when (val result = apiClient.fetchMemeTemplates()) {
            is NetworkResult.Success -> result.data
            is NetworkResult.Error -> null
        }
    }

    private suspend fun fetchZodiacHoroscopes(): List<Pair<String, NetworkResult<HoroscopeData>>> {
        return coroutineScope {
            Constants.zodiacSigns.map { (sign, _) ->
                async {
                    sign to apiClient.fetchDailyHoroscope(sign)
                }
            }.awaitAll()
        }
    }

    private fun createHoroscopesWithMemes(
        horoscopeResults: List<Pair<String, NetworkResult<HoroscopeData>>>,
        memeTemplates: List<Meme>?
    ): Map<String, DailyHoroscope> {
        return horoscopeResults.associate { (sign, result) ->
            val dates = Constants.zodiacSigns.first { it.first == sign }.second

            val horoscopeData = (result as? NetworkResult.Success)?.data
            val memeUrl = if (horoscopeData != null) {
                apiClient.createMemeUrl(
                    memes = memeTemplates,
                    sign = sign,
                    horoscope = horoscopeData.horoscope_data
                )
            } else {
                null
            }

            sign to DailyHoroscope(
                zodiacSign = sign,
                zodiacSignDates = dates,
                horoscope = horoscopeData,
                memeUrl = memeUrl
            )
        }
    }
}

