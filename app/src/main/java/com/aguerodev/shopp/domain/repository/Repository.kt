package com.aguerodev.shopp.domain.repository

import com.aguerodev.shopp.domain.entity.Product
import com.aguerodev.shopp.domain.entity.Country
import com.aguerodev.shopp.domain.entity.User

interface Repository{
    suspend fun getProduct(id: Int): Product
    suspend fun getProductList(country: Country): List<Product>
    suspend fun getHistoryProductList(): List<Product>
    suspend fun updateProductVisit(id: Int)
    suspend fun deleteHistoryProduct()
}