package com.turkcell.libraryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.turkcell.libraryapp.ui.screen.LoginScreen
import com.turkcell.libraryapp.ui.screen.RegisterScreen // Yeni ekranı import etmeyi unutma

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // Login ekranına navController'ı gönderiyoruz
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Screen.Register.route) {
            RegisterScreen(onNavigateToLogin = {
                navController.popBackStack() // Bir önceki sayfaya (Login) güvenli dönüş
            })
        }
    }
}