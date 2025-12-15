package com.aguerodev.shopp.view.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aguerodev.shopp.domain.entity.Product
import com.aguerodev.shopp.domain.useCase.GetHistoryProductListUseCase
import com.aguerodev.shopp.view.core.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoryProductListUseCase: GetHistoryProductListUseCase
) : ViewModel() {

    private val _purchaseHistoryState = MutableStateFlow<Resource<List<Product>>>(Resource.Idle())
    val purchaseHistoryState: StateFlow<Resource<List<Product>>> = _purchaseHistoryState

    init {
        loadPurchaseHistory()
    }

    private fun loadPurchaseHistory() {
        viewModelScope.launch {
            _purchaseHistoryState.value = Resource.Loading()
            try {
                val productList = getHistoryProductListUseCase()
                _purchaseHistoryState.value = Resource.Success(productList)
            } catch (e: Exception) {
                _purchaseHistoryState.value = Resource.Error(
                    message = e.localizedMessage ?: "Error desconocido al cargar el historial."
                )
            }
        }
    }

    fun refreshHistory() {
        loadPurchaseHistory()
    }
}