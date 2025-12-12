package com.aguerodev.shopp.view.core

import androidx.annotation.DrawableRes

sealed interface BottomBar {
    val route: String
    @get:DrawableRes val icon: Int
    val title: String

    data object Home : BottomBar {
        override val route: String = "home"
        override val icon: Int = android.R.drawable.ic_menu_compass
        override val title: String = "HOME"
    }

    data object History : BottomBar {
        override val route: String = "history"
        override val icon: Int = android.R.drawable.ic_menu_save
        override val title: String = "HISTORY"
    }

    data object Exit : BottomBar {
        override val route: String = "exit"
        override val icon: Int = android.R.drawable.ic_menu_manage
        override val title: String = "EXIT"
    }
}

val bottomDestinations = listOf(
    BottomBar.Home,
    BottomBar.History,
    BottomBar.Exit
)