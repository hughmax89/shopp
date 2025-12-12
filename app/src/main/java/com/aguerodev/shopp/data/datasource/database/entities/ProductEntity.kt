package com.aguerodev.shopp.data.datasource.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aguerodev.shopp.domain.entity.Product

@Entity(tableName = "product_table")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id: Int,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("price") val price: Double,
    @ColumnInfo("categoryName") val categoryName: String,
    @ColumnInfo("imageUrl") val imageUrl: String?,
    @ColumnInfo("rating") val rating: Double?,
    @ColumnInfo("ratingCount") val ratingCount: Int?,
    @ColumnInfo("visited") val visited: Boolean?
)


fun ProductTypeA.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        description = description,
        price = price.toDouble(),
        categoryName = category.name,
        imageUrl = images.firstOrNull(),
        rating = null,
        ratingCount = null,
        visited = false
    )
}

data class ProductTypeA(
    val id: Int,
    val title: String,
    val description: String,
    val price: Float,
    val category: Category,
    val images: List<String>
)

data class ProductTypeB(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val rating: Rating
)

data class Category(
    val name: String
)

data class Rating(
    val rate: Double,
    val count: Int
)

fun ProductTypeB.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        description = description,
        price = price,
        categoryName = category,
        imageUrl = "",
        rating = rating.rate,
        ratingCount = rating.count,
        visited = false
    )
}

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        price = price,
        categoryName = categoryName,
        imageUrl = imageUrl,
        rating = rating,
        ratingCount = ratingCount,
        visited = visited ?: false
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        description = description,
        price = price,
        categoryName = categoryName,
        imageUrl = imageUrl,
        rating = rating,
        ratingCount = ratingCount,
        visited = true
    )
}