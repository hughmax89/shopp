package com.aguerodev.shopp.data.datasource.network

import com.aguerodev.shopp.data.datasource.database.entities.ProductEntity
import com.aguerodev.shopp.domain.entity.Product
import retrofit2.http.GET

interface ShoppClientCountryA {
    @GET("products")
    suspend fun getProductListCountryA(): List<ProductEntity>
}

interface ShoppClientCountryB {
    @GET("en/rest/products")
    suspend fun getProductListCountryB(): List<ProductEntity>
}
