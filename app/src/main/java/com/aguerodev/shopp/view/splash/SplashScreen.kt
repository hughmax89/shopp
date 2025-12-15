package com.aguerodev.shopp.view.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private const val ANIMATION_DURATION_MS = 1000
private val ICON_SIZE = 150.dp

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen(navigateToLogin = {})
}

@Composable
fun SplashScreen(navigateToLogin: () -> Unit) {

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val animationProgress = remember { Animatable(0f) }

    val totalMovement = screenWidth + ICON_SIZE

    LaunchedEffect(Unit) {
        val animationJob = launch {
            animationProgress.animateTo(
                targetValue = 0.8f,
                animationSpec = tween(
                    durationMillis = ANIMATION_DURATION_MS,
                    easing = LinearEasing
                )
            )
        }

        animationJob.join()
        navigateToLogin()
    }

    val currentOffsetX = (-ICON_SIZE) + (totalMovement * animationProgress.value)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(ICON_SIZE)
                .offset(x = currentOffsetX)
        ) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = "Carrito de compras animado",
                tint = Color.White,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "SHOPP EXPRESS",
            fontSize = 48.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}