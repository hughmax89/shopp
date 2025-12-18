package com.aguerodev.shopp.domain.usecase

import com.aguerodev.shopp.domain.entity.Product
import com.aguerodev.shopp.domain.repository.Repository
import com.aguerodev.shopp.domain.useCase.GetHistoryProductListUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetHistoryProductListUseCaseTest {

    @RelaxedMockK
    lateinit var repository: Repository

    private lateinit var getHistoryProductListUseCase: GetHistoryProductListUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getHistoryProductListUseCase = GetHistoryProductListUseCase(repository)
    }

    @Test
    fun `cuando el repositorio devuelve una lista de productos el caso de uso debe retornar la misma lista`() = runTest {
        // GIVEN:
        val productList = listOf(
            Product(
                id = 0,
                title = "Smartphone Samsung",
                description = "El mejor smartphone del mercado",
                price = 2499.99,
                categoryName = "Electrónica",
                imageUrls = listOf("url1", "url2"),
                rating = 5.0,
                ratingCount = 150,
                visited = true,
                sale = false
            ),
            Product(
                id = 1,
                title = "Mouse Redragon",
                description = "Mouse Gamer  ",
                price = 299.99,
                categoryName = "Electrónica",
                imageUrls = listOf("url1", "url2"),
                rating = 4.8,
                ratingCount = 40,
                visited = true,
                sale = false
            )
        )

        coEvery { repository.getHistoryProductList() } returns productList

        // WHEN
        val result = getHistoryProductListUseCase()

        // THEN
        coVerify(exactly = 1) { repository.getHistoryProductList() }

        assertEquals(2, result.size)
        assertEquals("Smartphone Samsung", result[0].title)
        assertEquals(2499.99, result[0].price, 0.0)
        assertEquals(true, result[1].visited)
        assertEquals(false, result[1].sale)
    }

    @Test
    fun `cuando el repositorio devuelve una lista vacia el caso de uso debe retornar una lista vacia`() = runTest {
        // GIVEN
        coEvery { repository.getHistoryProductList() } returns emptyList()

        // WHEN
        val result = getHistoryProductListUseCase()

        // THEN
        assert(result.isEmpty())
        assertEquals(0, result.size)
    }
}