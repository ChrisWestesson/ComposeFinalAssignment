package com.example.composefinalassignment.data.models

import kotlinx.serialization.Serializable

@Serializable
data class HoroscopeResponse(
    val data: HoroscopeData
)