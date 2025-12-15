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
import androidx.navigation.toRoute
import com.aguerodev.shopp.view.detail.DetailProductScreen
import com.aguerodev.shopp.view.shopping.HistoryScreen
import com.aguerodev.shopp.view.shopping.HistoryViewModel
import com.aguerodev.shopp.view.home.HomeScreen
import com.aguerodev.shopp.view.home.HomeViewModel
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
            composable<Splash> {
                SplashScreen(navigateToLogin = {
                    navController.navigate(Login) {
                        popUpTo(0) { inclusive = true }
                    }
                })
            }

            composable<Login> {
                val viewModel = hiltViewModel<LoginViewModel>()
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = {
                        navController.navigate(BottomBar.Home.route) {
                            popUpTo(Login) { inclusive = true }
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
                val viewModel = hiltViewModel<HomeViewModel>()
                HomeScreen(
                    viewModel = viewModel,
                    navigateToDetail = { productId ->
                        navController.navigate(Detail(id = productId))
                    }
                )
            }

            composable(BottomBar.History.route) {
                val historyViewModel = hiltViewModel<HistoryViewModel>()
                HistoryScreen(
                    viewModel = historyViewModel
                )
            }

            composable<Detail> { backStackEntry ->
                val detailRoute = backStackEntry.toRoute<Detail>()
                DetailProductScreen(
                    productId = detailRoute.id,
                    onBack = { navController.popBackStack() },
                    onPurchaseComplete = {
                        navController.popBackStack()
                        navController.navigate(BottomBar.Home.route) {
                            popUpTo(BottomBar.Home.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(BottomBar.Exit.route) {
                navController.navigate(Login) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }
}