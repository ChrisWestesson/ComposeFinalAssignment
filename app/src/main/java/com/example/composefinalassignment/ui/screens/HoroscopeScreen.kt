package com.example.composefinalassignment.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.composefinalassignment.data.models.DailyHoroscope
import com.example.composefinalassignment.ui.components.ScreenHeader

@Composable
fun HoroscopeScreen(
    dailyHoroscope: DailyHoroscope,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        ScreenHeader(
            title = dailyHoroscope.zodiacSign,
            subtitle = dailyHoroscope.horoscope?.date.orEmpty()
        )

        HoroscopeMeme(
            memeUrl = dailyHoroscope.memeUrl.orEmpty(),
            sign = dailyHoroscope.zodiacSign
        )

        HoroscopeText(
            horoscopeText = dailyHoroscope.horoscope?.horoscope_data.orEmpty()
        )

        BackButton(onClick = onBackClick)
    }
}

@Composable
fun HoroscopeMeme(memeUrl: String, sign: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = memeUrl,
        contentDescription = "Meme for $sign",
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .aspectRatio(1.2f)
            .padding(bottom = 16.dp)
    )
}

@Composable
fun HoroscopeText(horoscopeText: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.8f),
                        Color.Transparent
                    )
                )
            )
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = horoscopeText,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
}

@Composable
fun BackButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp, horizontal = 16.dp)
    ) {
        Text(text = "Back")
    }
}