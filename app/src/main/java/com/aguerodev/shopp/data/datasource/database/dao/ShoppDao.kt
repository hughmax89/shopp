package com.aguerodev.shopp.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aguerodev.shopp.data.datasource.database.entities.ProductEntity

@Dao
interface ShoppDao {

    @Query("SELECT * FROM product_table WHERE id = :id")
    suspend fun getProductById(id: Int): ProductEntity

    @Query("SELECT * FROM product_table WHERE sale = 0")
    suspend fun getProducts(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Query("SELECT * FROM product_table WHERE visited = 1")
    suspend fun getAllHistoryProducts(): List<ProductEntity>

    @Query("UPDATE product_table SET visited = 1 WHERE id = :id")
    suspend fun updateProductVisited(id: Int)

    @Query("DELETE FROM product_table")
    suspend fun deleteAllHistoryProducts()

    @Query("UPDATE product_table SET sale = 1 WHERE id = :id")
    suspend fun saleProduct(id: Int)
}