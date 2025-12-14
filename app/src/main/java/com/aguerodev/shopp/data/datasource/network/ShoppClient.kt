package com.aguerodev.shopp.data.datasource.network

import com.aguerodev.shopp.data.datasource.database.entities.ProductEntity
import com.aguerodev.shopp.data.response.ProductResponseCountryA
import retrofit2.http.GET

interface ShoppClientCountryA {
    @GET("products")
    suspend fun getProductListCountryA(): List<ProductResponseCountryA>
}

interface ShoppClientCountryB {
    @GET("en/rest/products")
    suspend fun getProductListCountryB(): List<ProductEntity>
}
