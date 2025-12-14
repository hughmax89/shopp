package com.aguerodev.shopp.view.core

import BottomBarNavigation
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aguerodev.shopp.view.home.HomeScreen
import com.aguerodev.shopp.view.login.LoginScreen
import com.aguerodev.shopp.view.login.LoginViewModel
import com.aguerodev.shopp.view.splash.SplashScreen


@Composable
fun NavigationWrapper(
    modifier: Modifier,
    onBiometricLogin: ((onSuccess: () -> Unit) -> Unit),
    isBiometricReady: Boolean
) {
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
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

            // 🔹 SplashScreen
            composable<Splash> {
                SplashScreen(navigateToLogin = {
                    navController.navigate(Login) {
                        popUpTo(0) { inclusive = true }
                    }
                })
            }

            // 🔹 LoginScreen
            composable<Login> {
                val viewModel = hiltViewModel<LoginViewModel>()
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = {
                        navController.navigate(BottomBar.Home.route) {
                            popUpTo(Login) {
                                inclusive = true
                            }
                        }
                    },
                    onBiometricLoginRequest = {
                        onBiometricLogin {
                            navController.navigate(BottomBar.Home.route) {
                                popUpTo(Login) { inclusive = true }
                            }
                        }
                    },
                    isBiometricAvailable = isBiometricReady
                )
            }
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