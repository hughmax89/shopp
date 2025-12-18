package com.aguerodev.shopp.domain.usecase

import com.aguerodev.shopp.domain.entity.Product
import com.aguerodev.shopp.domain.repository.Repository
import com.aguerodev.shopp.domain.useCase.GetProductUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetProductUseCaseTest {

    @RelaxedMockK
    lateinit var repository: Repository

    private lateinit var getProductUseCase: GetProductUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getProductUseCase = GetProductUseCase(repository)
    }

    @Test
    fun `cuando se solicita un producto por ID debe retornar el producto correcto del repositorio`() = runTest {
        // GIVEN
        val productId = 505
        val expectedProduct = Product(
            id = productId,
            title = "Auriculares Sony XM5",
            description = "Cancelación de ruido líder en la industria",
            price = 350.0,
            categoryName = "Audio",
            imageUrls = listOf("https://image.com/sony"),
            rating = 4.9,
            ratingCount = 1200,
            visited = false,
            sale = true
        )

        coEvery { repository.getProduct(productId) } returns expectedProduct

        // WHEN
        val result = getProductUseCase(productId)

        // THEN
        coVerify(exactly = 1) { repository.getProduct(productId) }


        assertEquals(productId, result.id)
        assertEquals("Auriculares Sony XM5", result.title)
        assertEquals(350.0, result.price, 0.0)
        assertEquals(true, result.sale)
    }
}