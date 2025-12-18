package com.aguerodev.shopp.domain.usecase

import com.aguerodev.shopp.domain.repository.Repository
import com.aguerodev.shopp.domain.useCase.BuyProductUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class BuyProductUseCaseTest {

    @RelaxedMockK
    lateinit var repository: Repository

    private lateinit var buyProductUseCase: BuyProductUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        buyProductUseCase = BuyProductUseCase(repository)
    }

    @Test
    fun `cuando el caso de uso es llamado debe llamar al repositorio una vez`() = runTest {
        // Given
        val productId = 101

        coEvery { repository.buyProductUseCase(productId) } returns Unit

        // When
        buyProductUseCase(productId)

        // Then
        coVerify(exactly = 1) { repository.buyProductUseCase(productId) }
    }
}