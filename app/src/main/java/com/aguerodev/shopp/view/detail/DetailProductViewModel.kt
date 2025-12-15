package com.aguerodev.shopp.view.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aguerodev.shopp.domain.entity.Country
import com.aguerodev.shopp.domain.entity.Product
import com.aguerodev.shopp.domain.useCase.BuyProductUseCase
import com.aguerodev.shopp.domain.useCase.GetProductUseCase
import com.aguerodev.shopp.domain.useCase.UpdateProductVisitedUseCase
import com.aguerodev.shopp.view.core.DetailNavigationState
import com.aguerodev.shopp.view.core.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val buyProductUseCase: BuyProductUseCase,
    private val updateProductVisitedUseCase: UpdateProductVisitedUseCase
) : ViewModel() {

    private val _productState = MutableStateFlow<Resource<Product>>(Resource.Idle())
    val productState: StateFlow<Resource<Product>> = _productState

    private val _navigationState = MutableStateFlow(DetailNavigationState.DETAIL)
    val navigationState: StateFlow<DetailNavigationState> = _navigationState

    private val _selectedCountry = MutableStateFlow(Country.COUNTRY_A)
    val selectedCountry: StateFlow<Country> = _selectedCountry

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            _productState.value = Resource.Loading()
            try {
                val product = getProductUseCase(productId)

                if (product != null) {
                    updateProductVisitedUseCase(productId)
                    val updatedProduct = product.copy(visited = true)
                    _productState.value = Resource.Success(updatedProduct)

                } else {
                    _productState.value = Resource.Error("Producto no encontrado.")
                }
            } catch (e: Exception) {
                _productState.value = Resource.Error("Error al cargar detalles: ${e.localizedMessage}")
            }
        }
    }

    fun setNavigationState(state: DetailNavigationState) {
        _navigationState.value = state
    }

    fun markProductAsSold(productId: Int) {
        viewModelScope.launch {
            try {
                buyProductUseCase(productId)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}