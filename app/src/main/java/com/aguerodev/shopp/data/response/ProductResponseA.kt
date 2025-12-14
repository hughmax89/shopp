package com.aguerodev.shopp.data.response

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.aguerodev.shopp.data.datasource.database.ListConverter
import com.aguerodev.shopp.data.datasource.database.entities.ProductEntity
import kotlinx.serialization.Serializable


@Serializable
data class ProductResponseA(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val image: String,
    val category: String,
    val rating: RatingResponse
)
@Serializable
data class RatingResponse(
    val rate: Double,
    val count: Int
)
fun ProductResponseA.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        price = this.price,
        categoryName = this.category,
        imageUrls = listOf(this.image),
        rating = this.rating.rate,
        ratingCount = this.rating.count,
        visited = false
    )
}