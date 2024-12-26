package com.example.composefinalassignment.data.models

data class DailyHoroscope(
    val zodiacSign: String,
    val zodiacSignDates: String,
    val horoscope: HoroscopeData? = null,
    val memeUrl: String? = null
) {
    val isValid: Boolean
        get() = !horoscope?.horoscope_data.isNullOrEmpty() && !memeUrl.isNullOrEmpty()
}
