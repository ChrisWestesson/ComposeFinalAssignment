package com.example.composefinalassignment.data.models

import kotlinx.serialization.Serializable

@Serializable
data class HoroscopeData(
    val date: String,
    val horoscope_data: String
)
