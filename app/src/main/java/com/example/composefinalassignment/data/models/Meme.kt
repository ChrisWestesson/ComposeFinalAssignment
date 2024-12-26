package com.example.composefinalassignment.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Meme(
    val url: String,
    val template: String
)