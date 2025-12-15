package com.aguerodev.shopp.view.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import com.aguerodev.shopp.domain.entity.Country

data class CountryColorScheme(
    val primaryColor: Color,
    val onPrimaryColor: Color
)

@Composable
fun getAestheticColorScheme(): CountryColorScheme {
    return CountryColorScheme(
        primaryColor = Color(0xFF1C1C1E),
        onPrimaryColor = Color(0xFFF2F2F7)
    )
}