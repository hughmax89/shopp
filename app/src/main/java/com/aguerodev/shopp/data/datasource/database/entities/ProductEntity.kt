package com.aguerodev.shopp.data.datasource.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.aguerodev.shopp.data.datasource.database.ListConverter
import com.aguerodev.shopp.domain.entity.Product

@Entity(tableName = "product_table")
@TypeConverters(ListConverter::class)
data class ProductEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val categoryName: String,
    val imageUrls: List<String>,
    val rating: Double,
    val ratingCount: Int,
    val visited: Boolean,
    val sale: Boolean
)

data class CategoryEntity(
    val id: Int,
    val name: String,
    val image: String,
    val slug: String
)

fun ProductEntity.toDomain(): Product {
    return Product(
        id = this.id,
        title = this.title,
        description = this.description,
        price = this.price,
        categoryName = this.categoryName,
        imageUrls = this.imageUrls,
        rating = this.rating,
        ratingCount = this.ratingCount,
        visited = this.visited,
        sale = this.sale
    )
}