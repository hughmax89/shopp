package com.aguerodev.shopp.domain.usecase

import com.aguerodev.shopp.domain.entity.User
import com.aguerodev.shopp.domain.repository.AuthRepository
import com.aguerodev.shopp.domain.useCase.LoginUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {

    @RelaxedMockK
    lateinit var repository: AuthRepository

    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        loginUseCase = LoginUseCase(repository)
    }

    @Test
    fun `cuando el login es exitoso debe retornar el token del usuario`() = runTest {
        // GIVEN
        val user = User(email = "test@mail.com", password = "pass123")
        val expectedToken = "fake-jwt-token-abc-123"

        coEvery { repository.loginUser(user) } returns expectedToken

        // WHEN
        val result = loginUseCase(user)

        // THEN
        coVerify(exactly = 1) { repository.loginUser(user) }

        assertEquals(expectedToken, result)
    }
}