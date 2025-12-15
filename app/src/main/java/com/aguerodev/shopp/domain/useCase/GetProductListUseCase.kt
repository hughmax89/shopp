package com.aguerodev.shopp.domain.useCase

import com.aguerodev.shopp.domain.entity.Product
import com.aguerodev.shopp.domain.entity.Country
import com.aguerodev.shopp.domain.repository.Repository
import javax.inject.Inject

class GetProductListUseCase @Inject constructor(val repository: Repository) {
    suspend operator fun invoke(country: Country): List<Product>{
        return repository.getProductList(country)
    }
}