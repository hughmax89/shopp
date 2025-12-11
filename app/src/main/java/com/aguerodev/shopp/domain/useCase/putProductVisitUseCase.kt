package com.aguerodev.shopp.domain.useCase

import com.aguerodev.shopp.domain.entity.Product
import com.aguerodev.shopp.domain.repository.Repository
import jakarta.inject.Inject

class putProductVisitUseCase @Inject constructor(val repository: Repository) {
    suspend operator fun invoke(product: Product){
        return repository.putProductVisit(product)
    }
}