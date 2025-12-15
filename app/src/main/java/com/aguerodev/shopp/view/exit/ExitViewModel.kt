package com.aguerodev.shopp.view.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aguerodev.shopp.domain.useCase.LogoutUserUseCase
import com.aguerodev.shopp.view.util.DataStoreManager
import com.aguerodev.shopp.view.core.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias LogoutResource = Resource<Unit>

@HiltViewModel
class ExitViewModel @Inject constructor(
    private val logoutUserUseCase: LogoutUserUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _logoutState = MutableStateFlow<LogoutResource>(Resource.Idle())
    val logoutState: StateFlow<LogoutResource> = _logoutState

    fun clearDataAndLogout() {
        viewModelScope.launch(Dispatchers.IO) {

            _logoutState.value = Resource.Loading()

            try {
                logoutUserUseCase.invoke()

                dataStoreManager.clearAllPreferences()

                _logoutState.value = Resource.Success(Unit)

            } catch (e: Exception) {
                _logoutState.value = Resource.Error(
                    message = e.localizedMessage ?: "Fallo desconocido al cerrar sesión."
                )
            }
        }
    }

    fun resetState() {
        _logoutState.value = Resource.Idle()
    }
}