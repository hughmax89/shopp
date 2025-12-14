package com.aguerodev.shopp.view.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aguerodev.shopp.domain.entity.Country
import com.aguerodev.shopp.domain.entity.Product
import com.aguerodev.shopp.domain.useCase.GetProductListUseCase
import com.aguerodev.shopp.view.core.Resource
import com.aguerodev.shopp.view.util.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val getProductListUseCase: GetProductListUseCase
) : ViewModel() {

    private val _selectedCountry = MutableStateFlow(Country.COUNTRY_A)
    val selectedCountry: StateFlow<Country> = _selectedCountry.asStateFlow()

    private val _productsState = MutableStateFlow<Resource<List<Product>>>(Resource.Idle())
    val productsState: StateFlow<Resource<List<Product>>> = _productsState.asStateFlow()

    var isNavigating by mutableStateOf(false)
        private set

    init {
        loadCountryPreferences()
    }

    private fun loadCountryPreferences() {
        viewModelScope.launch {
            dataStoreManager.userPreferencesFlow.collect { (_, _, savedCountry) ->
                val countryToUse = savedCountry ?: Country.COUNTRY_A
                _selectedCountry.value = countryToUse
                if (_productsState.value is Resource.Idle || _productsState.value is Resource.Error) {
                    loadProducts(countryToUse)
                }
            }
        }
    }

    private fun loadProducts(country: Country) {
        _productsState.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val products = getProductListUseCase(country)
                _productsState.value = Resource.Success(products)
            } catch (e: Exception) {
                _productsState.value = Resource.Error(e.message ?: "Error al cargar productos")
            }
        }
    }


    fun refreshProducts() {
        loadProducts(_selectedCountry.value)
    }

    fun onProductClicked(product: Product, navigateToDetail: (Int) -> Unit) {
        isNavigating = true
        viewModelScope.launch {
            // Simular un pequeño delay de carga/navegación
            delay(500)
            navigateToDetail(product.id)
            isNavigating = false
        }
    }
}