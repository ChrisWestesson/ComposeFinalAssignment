package com.example.composefinalassignment.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.composefinalassignment.data.models.DailyHoroscope
import com.example.composefinalassignment.ui.components.ScreenHeader

@Composable
fun ZodiacSignsScreen(
    zodiacItems: Map<String, DailyHoroscope>,
    onZodiacClick: (String) -> Unit,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ScreenHeader(
            title = "Memeoscope",
            subtitle = "What does my horoscope meme?"
        )

        ZodiacSignGrid(
            zodiacItems = zodiacItems,
            onZodiacClick = onZodiacClick,
            modifier = Modifier.weight(1f)
        )

        RefreshButton(onRefreshClick = onRefreshClick)
    }
}

@Composable
fun ZodiacSignGrid(
    zodiacItems: Map<String, DailyHoroscope>,
    onZodiacClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(zodiacItems.values.toList()) { dailyHoroscope ->
            ZodiacSignCard(
                dailyHoroscope = dailyHoroscope,
                onClick = { onZodiacClick(dailyHoroscope.zodiacSign) }
            )
        }
    }
}

@Composable
fun ZodiacSignCard(
    dailyHoroscope: DailyHoroscope,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(Color.Transparent)
            .clickable(
                enabled = dailyHoroscope.isValid,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ZodiacSignCardHeader(
            sign = dailyHoroscope.zodiacSign,
            dateRange = dailyHoroscope.zodiacSignDates
        )

        if (dailyHoroscope.isValid) {
            ZodiacSignCardMeme(
                memeUrl = dailyHoroscope.memeUrl.orEmpty(),
                sign = dailyHoroscope.zodiacSign,
                modifier = Modifier.weight(1f)
            )
        } else {
            Text(
                text = "Horoscope unavailable.",
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f).
                    padding(vertical = 16.dp)
            )
        }
    }
}

@Composable
fun ZodiacSignCardHeader(
    sign: String,
    dateRange: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.8f))
    ) {
        Column {
            Text(
                text = sign,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = dateRange,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ZodiacSignCardMeme(
    memeUrl: String?,
    sign: String,
    modifier: Modifier = Modifier
) {
    var isImageLoaded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = memeUrl ?: "",
            contentDescription = "Meme for $sign",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            onState = { state ->
                isImageLoaded = state is AsyncImagePainter.State.Success
            }
        )

        if (!isImageLoaded) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun RefreshButton(onRefreshClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onRefreshClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        Text(text = "Refresh")
    }
}