package com.aguerodev.shopp.data.repository

import com.aguerodev.shopp.data.datasource.database.AppDataBase
import com.aguerodev.shopp.data.datasource.database.entities.CountrySelectedEntity
import com.aguerodev.shopp.data.datasource.database.entities.ProductEntity
import com.aguerodev.shopp.data.datasource.database.entities.toDomain
import com.aguerodev.shopp.data.datasource.network.ShoppClientCountryA
import com.aguerodev.shopp.data.datasource.network.ShoppClientCountryB
import com.aguerodev.shopp.data.di.DataModule
import com.aguerodev.shopp.data.response.toEntity
import com.aguerodev.shopp.domain.entity.Product
import com.aguerodev.shopp.domain.entity.Country
import com.aguerodev.shopp.domain.entity.User
import com.aguerodev.shopp.domain.repository.Repository
import javax.inject.Inject

class RepositoryImplementation @Inject constructor(
    @DataModule.CountryAApi private val shoppClientCountryA: ShoppClientCountryA,
    @DataModule.CountryBApi private val shoppClientCountryB: ShoppClientCountryB,
    private val appDataBase: AppDataBase
) : Repository {
    override suspend fun getProduct(id: Int): Product {
        return appDataBase.shoppDao().getProductById(id).toDomain()
    }

    override suspend fun getProductList(countryCountry: Country): List<Product> {
        var productList = appDataBase.shoppDao().getProducts()

        if (productList.isEmpty()) {
            when (countryCountry) {
                Country.COUNTRY_A -> {
                    productList = shoppClientCountryA
                        .getProductListCountryA()
                        .map { it.toEntity() }
                }

                Country.COUNTRY_B -> {
                    productList = shoppClientCountryB
                        .getProductListCountryB()
                        .map { it.toEntity() }
                }
            }
            appDataBase.shoppDao().insertProducts(productList)
        }

        return appDataBase.shoppDao().getProducts().map { it.toDomain() }
    }


    override suspend fun getHistoryProductList(): List<Product> {
        return appDataBase.shoppDao().getAllHistoryProducts().map { it.toDomain() }
    }

    override suspend fun updateProductVisit(id: Int) {
        appDataBase.shoppDao().updateProductVisited(id)
    }

    override suspend fun deleteHistoryProduct() {
        appDataBase.shoppDao().deleteHistoryProduct()
    }

    override suspend fun buyProductUseCase(id: Int) {
        appDataBase.shoppDao().saleProduct(id)
    }
}