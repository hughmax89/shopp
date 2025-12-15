package com.aguerodev.shopp.domain.useCase

import com.aguerodev.shopp.domain.repository.Repository
import jakarta.inject.Inject

class BuyProductUseCase @Inject constructor(val repository: Repository){
    suspend operator fun invoke(id: Int){
        return repository.buyProductUseCase(id)
    }
}