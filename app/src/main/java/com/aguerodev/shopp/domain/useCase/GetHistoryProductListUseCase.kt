package com.aguerodev.shopp.domain.useCase

import com.aguerodev.shopp.domain.entity.Product
import com.aguerodev.shopp.domain.repository.Repository
import javax.inject.Inject

class GetHistoryProductListUseCase @Inject constructor(val repository: Repository) {
    suspend operator fun invoke(): List<Product>{
        return repository.getHistoryProductList()
    }
}