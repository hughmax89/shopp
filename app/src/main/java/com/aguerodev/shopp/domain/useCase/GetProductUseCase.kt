package com.aguerodev.shopp.domain.useCase

import com.aguerodev.shopp.domain.entity.Product
import com.aguerodev.shopp.domain.repository.Repository
import jakarta.inject.Inject

class GetProductUseCase @Inject constructor(val repository: Repository){
    suspend operator fun invoke(id: Int): Product{
        return repository.getProduct(id)
    }
}