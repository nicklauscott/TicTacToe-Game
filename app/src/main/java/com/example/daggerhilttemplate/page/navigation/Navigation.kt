package com.example.daggerhilttemplate.page.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.daggerhilttemplate.constant.Screens
import com.example.daggerhilttemplate.page.main.HomeScreen
import com.example.daggerhilttemplate.page.main.TicTacToeViewModel
import com.example.daggerhilttemplate.page.settings.SettingsScreen
import com.example.daggerhilttemplate.page.settings.SettingsViewModel
import com.example.daggerhilttemplate.page.splash.SplashScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.SplashScreen.route) {
        composable(route = Screens.SplashScreen.route) {
            SplashScreen(navController = navController)
        }

        composable(route = Screens.HomeScreen.route) {
            val viewModel = hiltViewModel<TicTacToeViewModel>()
            HomeScreen(navController = navController,
                viewModel = viewModel)
        }

        composable(route = Screens.SettingsScreen.route) {
            val viewModel = hiltViewModel<SettingsViewModel>()
            SettingsScreen(navController = navController, viewModel)
        }
    }
}