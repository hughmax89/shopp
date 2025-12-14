package com.aguerodev.shopp.data.response

import com.aguerodev.shopp.data.datasource.database.entities.ProductEntity
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponseCountryA(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: RatingResponseCountryA
)

@Serializable
data class RatingResponseCountryA(
    val rate: Double,
    val count: Int
)

fun ProductResponseCountryA.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        title = this.title,
        price = this.price,
        description = this.description,
        categoryName = this.category,
        imageUrl = this.image,
        rating = this.rating.rate,
        ratingCount = this.rating.count,
        visited = false
    )
}