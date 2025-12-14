package com.aguerodev.shopp.data.repository

import com.aguerodev.shopp.data.datasource.database.AppDataBase
import com.aguerodev.shopp.data.datasource.database.entities.toDomain
import com.aguerodev.shopp.data.datasource.network.ShoppClientCountryA
import com.aguerodev.shopp.data.response.toEntity
import com.aguerodev.shopp.domain.entity.Product
import com.aguerodev.shopp.domain.entity.Country
import com.aguerodev.shopp.domain.entity.User
import com.aguerodev.shopp.domain.repository.Repository
import javax.inject.Inject

class RepositoryImplementation @Inject constructor(
    private val shoppClientCountryA: ShoppClientCountryA,
//    @DataModule.CountryBApi private val shoppClientCountryB: ShoppClientCountryB,
    private val appDataBase: AppDataBase
) : Repository {
        override suspend fun getProduct(id: Int): Product {
            return appDataBase.shoppDao().getProductById(id).toDomain()
        }

    override suspend fun getProductList(countryCountry: Country): List<Product> {
        val productList = shoppClientCountryA.getProductListCountryA().map { it.toEntity() }
//        val productList = when (userCountry) {
//            User.COUNTRY_A -> shoppClientCountryA.getProductListCountryA()
//            User.COUNTRY_B -> shoppClientCountryB.getProductListCountryB()
//        }
        appDataBase.shoppDao().insertProducts(productList)

        return appDataBase.shoppDao().getProducts().map { it.toDomain() }
    }


    override suspend fun getHistoryProductList(): List<Product> {
            return appDataBase.shoppDao().getAllHistoryProducts().map { it.toDomain() }
        }

        override suspend fun updateProductVisit(id: Int) {
            appDataBase.shoppDao().updateProductVisited(id)
        }

        override suspend fun deleteHistoryProduct() {
            appDataBase.shoppDao().deleteAllHistoryProducts()
        }
}