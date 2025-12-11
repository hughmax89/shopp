package com.aguerodev.shopp.domain.repository

import com.aguerodev.shopp.domain.entity.Product
import com.aguerodev.shopp.domain.entity.User

interface Repository{
    suspend fun getProduct(id: Int): Product
    suspend fun getProductList(user: User): List<Product>
    suspend fun getHistoryProductList(): List<Product>
    suspend fun putProductVisit(product: Product)
    suspend fun deleteHistoryProduct()
}