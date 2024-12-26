package com.example.composefinalassignment.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.composefinalassignment.data.models.DailyHoroscope
import com.example.composefinalassignment.data.state.UiState
import com.example.composefinalassignment.ui.screens.HoroscopeScreen
import com.example.composefinalassignment.ui.screens.ZodiacSignsScreen
import com.example.composefinalassignment.viewmodels.HoroscopeViewModel

@Composable
fun MemeoscopeNavHost(
    navController: NavHostController,
    uiState: UiState<Map<String, DailyHoroscope>>,
    viewModel: HoroscopeViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = "zodiac_sign_screen") {
        composable("zodiac_sign_screen") {
            ZodiacSignsScreen(
                zodiacItems = (uiState as? UiState.Success)?.data ?: emptyMap(),
                onZodiacClick = { sign ->
                    navController.navigate("horoscope/$sign")
                },
                onRefreshClick = viewModel::fetchAllHoroscopes,
                modifier = modifier
            )
        }

        composable(
            "horoscope/{sign}",
            arguments = listOf(navArgument("sign") { type = NavType.StringType })
        ) { backStackEntry ->
            val sign = backStackEntry.arguments?.getString("sign").orEmpty()
            val dailyHoroscope = (uiState as? UiState.Success)?.data?.get(sign)

            if (dailyHoroscope?.isValid == true) {
                HoroscopeScreen(
                    dailyHoroscope = dailyHoroscope,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}