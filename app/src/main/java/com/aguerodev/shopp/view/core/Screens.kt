package com.aguerodev.shopp.view.core

import kotlinx.serialization.Serializable

@Serializable
object Splash

@Serializable
object Login

@Serializable
object Home

@Serializable
data class Detail(val id: Int)

@Serializable
object History