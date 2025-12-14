package com.aguerodev.shopp.domain.useCase

import com.aguerodev.shopp.domain.entity.User
import com.aguerodev.shopp.domain.repository.AuthRepository
import jakarta.inject.Inject

class LoginUseCase @Inject constructor(val repository: AuthRepository){
    suspend operator fun invoke(user: User): String{
        return repository.loginUser(user)
    }
}