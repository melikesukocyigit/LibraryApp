package com.turkcell.libraryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.turkcell.libraryapp.ui.screen.LoginScreen
import com.turkcell.libraryapp.ui.screen.RegisterScreen
import com.turkcell.libraryapp.ui.screen.HomeScreen
import com.turkcell.libraryapp.ui.screen.SplashScreen
import com.turkcell.libraryapp.ui.screen.BorrowsScreen // YENİ SAYFAMIZI İÇERİ AKTARDIK
import com.turkcell.libraryapp.ui.viewmodel.AuthViewModel
import com.turkcell.libraryapp.ui.viewmodel.BookViewModel

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    val authViewModel: AuthViewModel = viewModel()
    val bookViewModel: BookViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // SPLASH EKRANI
        composable(Screen.Splash.route) {
            SplashScreen(authViewModel,
                onAuthenticated = { role ->
                    navController.navigate(Screen.Homepage.route){
                        popUpTo(Screen.Splash.route) {inclusive=true}
                    }
                },
                onUnauthenticated = {
                    navController.navigate(Screen.Login.route)
                    {
                        popUpTo(Screen.Splash.route) {inclusive=true}
                    }
                })
        }

        // GİRİŞ EKRANI (Çift yazım temizlendi)
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = { role ->
                    navController.navigate(Screen.Homepage.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }

        // KAYIT EKRANI
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onRegisterSuccess = {
                    navController.navigate(Screen.Homepage.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }

        // ANA SAYFA
        composable(Screen.Homepage.route) {
            HomeScreen(
                authViewModel = authViewModel,
                bookViewModel = bookViewModel,
                onNavigateToBorrows = { navController.navigate("borrows") }
            )
        }


        // YENİ: KİRALAMALAR SAYFASI
        composable("borrows") {
            BorrowsScreen(
                authViewModel = authViewModel,
                bookViewModel = bookViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}