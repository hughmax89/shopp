package com.aguerodev.shopp.view.core

import androidx.annotation.DrawableRes
import com.aguerodev.shopp.R


sealed class BottomBar(
    val route: String,
    val title: String,
    @DrawableRes val icon: Int // Usamos Int para IDs de recursos Drawable
) {
    object Home : BottomBar("home", "Home", R.drawable.ic_list_shop)
    object Shopping : BottomBar("history", "Shopping", R.drawable.shopping_cart)
    object Exit : BottomBar("exit", "Salir", R.drawable.ic_exit)
}

val bottomDestinations = listOf(BottomBar.Home, BottomBar.Shopping, BottomBar.Exit)