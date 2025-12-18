package com.aguerodev.shopp.domain.usecase

import com.aguerodev.shopp.domain.repository.Repository
import com.aguerodev.shopp.domain.useCase.UpdateProductVisitedUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateProductVisitedUseCaseTest {

    @RelaxedMockK
    lateinit var repository: Repository

    private lateinit var updateProductVisitedUseCase: UpdateProductVisitedUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        updateProductVisitedUseCase = UpdateProductVisitedUseCase(repository)
    }

    @Test
    fun `cuando se visita un producto debe llamar al repositorio para actualizar su estado`() = runTest {
        // GIVEN
        val productId = 77

        coEvery { repository.updateProductVisit(productId) } returns Unit

        // WHEN
        updateProductVisitedUseCase(productId)

        // THEN
        coVerify(exactly = 1) { repository.updateProductVisit(productId) }
    }
}