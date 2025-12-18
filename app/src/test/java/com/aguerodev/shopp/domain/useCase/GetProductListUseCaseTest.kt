package com.aguerodev.shopp.domain.usecase

import com.aguerodev.shopp.domain.entity.Country
import com.aguerodev.shopp.domain.entity.Product
import com.aguerodev.shopp.domain.repository.Repository
import com.aguerodev.shopp.domain.useCase.GetProductListUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetProductListUseCaseTest {

    @RelaxedMockK
    lateinit var repository: Repository

    private lateinit var getProductListUseCase: GetProductListUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getProductListUseCase = GetProductListUseCase(repository)
    }

    @Test
    fun `cuando se solicita la lista de Argentina debe llamar al repositorio con el pais correcto`() = runTest {
        // GIVEN
        val countryRequested = Country.COUNTRY_A
        val fakeProducts = listOf(
            Product(
                id = 1, title = "Mate", description = "Mate Camionero",
                price = 25.0, categoryName = "Viaje", imageUrls = emptyList(),
                rating = 5.0, ratingCount = 10, sale = true
            ),
            Product(
                id = 2, title = "Bombilla", description = "Bombilla de acero",
                price = 5.0, categoryName = "Viaje", imageUrls = emptyList(),
                rating = 4.8, ratingCount = 7, sale = true
            )
        )

        coEvery { repository.getProductList(countryRequested) } returns fakeProducts

        // WHEN
        val result = getProductListUseCase(countryRequested)

        // THEN
        coVerify(exactly = 1) { repository.getProductList(countryRequested) }

        assertEquals(2, result.size)
        assertEquals("Mate", result[0].title)
    }

    @Test
    fun `cuando se solicita la lista de Brasil debe retornar los productos de ese pais`() = runTest {
        // GIVEN
        val countryRequested = Country.COUNTRY_B
        val fakeProducts = listOf(
            Product(
                id = 2, title = "Caipirinha", description = "bebida alcoholica",
                price = 20.0, categoryName = "Bebida", imageUrls = emptyList(),
                rating = 4.0, ratingCount = 70, sale = false
            ),
            Product(
                id = 2, title = "Camiseta Brasil", description = "100% Algodón",
                price = 250.0, categoryName = "Ropa", imageUrls = emptyList(),
                rating = 4.7, ratingCount = 200, sale = true
            )
        )

        coEvery { repository.getProductList(Country.COUNTRY_B) } returns fakeProducts

        // WHEN
        val result = getProductListUseCase(countryRequested)

        // THEN
        coVerify(exactly = 1) { repository.getProductList(Country.COUNTRY_B) }
        assertEquals("Caipirinha", result[0].title)
        assertEquals(250.0, result[1].price, 0.0)
        assertEquals(true, result[1].sale)

    }
}