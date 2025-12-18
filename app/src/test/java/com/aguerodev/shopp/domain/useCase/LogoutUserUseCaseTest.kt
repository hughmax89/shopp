package com.aguerodev.shopp.domain.usecase

import com.aguerodev.shopp.domain.repository.Repository
import com.aguerodev.shopp.domain.useCase.LogoutUserUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LogoutUserUseCaseTest {

    @RelaxedMockK
    lateinit var repository: Repository

    private lateinit var logoutUserUseCase: LogoutUserUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        logoutUserUseCase = LogoutUserUseCase(repository)
    }

    @Test
    fun `cuando el usuario cierra sesion se debe llamar al repositorio para borrar el historial`() = runTest {
        // GIVEN
        coEvery { repository.deleteHistoryProduct() } returns Unit

        // WHEN
        logoutUserUseCase()

        // THEN
        coVerify(exactly = 1) { repository.deleteHistoryProduct() }
    }
}