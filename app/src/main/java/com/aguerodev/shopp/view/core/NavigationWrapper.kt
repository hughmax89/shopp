package com.aguerodev.shopp.view.core

import BottomBarNavigation
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aguerodev.shopp.view.home.HomeScreen
import com.aguerodev.shopp.view.login.LoginScreen
import com.aguerodev.shopp.view.splash.SplashScreen

@Composable
fun NavigationWrapper(modifier: Modifier) {
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    // ¿En esta ruta se debe mostrar el bottom bar?
    val showBottomBar = bottomDestinations.any { it.route == currentDestination?.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBarNavigation(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Splash,
            modifier = modifier.padding(innerPadding)
        ) {

            // 🔹 SplashScreen: primera pantalla
            composable<Splash> {
                SplashScreen(navigateToLogin = {
                    navController.navigate(Login) {
                        popUpTo(0) { inclusive = true }
                    }
                })
            }

            composable<Login> {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(BottomBar.Home.route) {
                            popUpTo(Login) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            // 🔹 Home + pantallas del bottom bar
            composable(BottomBar.Home.route) {
                HomeScreen()
            }

            composable(BottomBar.History.route) {
//                HistoryScreen()
            }

            composable(BottomBar.Exit.route) {
                navController.navigate(Login) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }
}

