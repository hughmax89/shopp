package com.aguerodev.shopp.domain.entity

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val categoryName: String,
    val imageUrl: String?,
    val rating: Double?,
    val ratingCount: Int?,
    val visited: Boolean
)
