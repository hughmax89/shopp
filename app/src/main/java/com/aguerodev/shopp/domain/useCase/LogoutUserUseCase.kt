package com.aguerodev.shopp.domain.useCase

import com.aguerodev.shopp.domain.repository.Repository
import jakarta.inject.Inject

class LogoutUserUseCase @Inject constructor(val repository: Repository) {
    suspend operator fun invoke(){
        return repository.deleteHistoryProduct()
    }
}