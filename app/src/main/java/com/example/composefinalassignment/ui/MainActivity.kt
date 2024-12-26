package com.example.composefinalassignment.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.composefinalassignment.ui.theme.ComposeFinalAssignmentTheme
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.composefinalassignment.data.state.UiState
import com.example.composefinalassignment.viewmodels.HoroscopeViewModel
import androidx.navigation.compose.rememberNavController
import com.example.composefinalassignment.ui.components.BackgroundImage
import com.example.composefinalassignment.ui.components.ErrorMessage
import com.example.composefinalassignment.ui.components.LoadingIndicator
import com.example.composefinalassignment.ui.navigation.MemeoscopeNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeFinalAssignmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MemeoscopeApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MemeoscopeApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val viewModel = remember { HoroscopeViewModel() }
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage()

        when (uiState) {
            is UiState.Loading -> LoadingIndicator()
            is UiState.Error -> ErrorMessage(
                message = (uiState as UiState.Error).message,
                onRetry = viewModel::fetchAllHoroscopes
            )
            is UiState.Success -> MemeoscopeNavHost(
                navController = navController,
                uiState = uiState,
                viewModel = viewModel,
                modifier = modifier
            )
        }
    }
}