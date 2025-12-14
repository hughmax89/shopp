package com.aguerodev.shopp.data.response

import com.aguerodev.shopp.data.datasource.database.entities.ProductEntity
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponseB(
    val id: Int,
    val title: String,
    val slug: String,
    val price: Int,
    val description: String,
    val category: CategoryBResponse,
    val images: List<String>
)

@Serializable
data class CategoryBResponse(
    val id: Int,
    val name: String,
    val image: String,
    val slug: String
)

fun ProductResponseB.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        price = this.price.toDouble(),
        categoryName = this.category.name,
        imageUrls = this.images,
        rating = 0.0,
        ratingCount = 0,
        visited = false
    )
}