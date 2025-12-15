package com.aguerodev.shopp.data.datasource.network


import com.aguerodev.shopp.data.response.ProductResponseA
import com.aguerodev.shopp.data.response.ProductResponseB
import retrofit2.http.GET

interface ShoppClientCountryA {
    @GET("products")
    suspend fun getProductListCountryA(): List<ProductResponseA>
}

interface ShoppClientCountryB {
    @GET("api/v1/products")
    suspend fun getProductListCountryB(): List<ProductResponseB>
}
