package com.aguerodev.shopp.view.login

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aguerodev.shopp.domain.entity.Country
import com.aguerodev.shopp.domain.entity.User
import com.aguerodev.shopp.domain.useCase.LoginUseCase
import com.aguerodev.shopp.view.core.Resource
import com.aguerodev.shopp.view.util.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val dataStoreManager: DataStoreManager,
    private val loginUseCase: LoginUseCase
) : AndroidViewModel(application) {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()
    private val _selectedCountry = MutableStateFlow(Country.COUNTRY_A)
    val selectedCountry: StateFlow<Country> = _selectedCountry.asStateFlow()
    private val _loginState = MutableStateFlow<Resource<String>>(Resource.Idle())
    val loginState: StateFlow<Resource<String>> = _loginState.asStateFlow()
    private val _validationError = MutableStateFlow<String?>(null)
    val validationError: StateFlow<String?> = _validationError.asStateFlow()

    var rememberMe by mutableStateOf(false)
    var isUserRemembered by mutableStateOf(false)
    var rememberedUserName by mutableStateOf<String?>(null)


    val isEmailFormatValid: StateFlow<Boolean> = _email.map { email ->
        email.isEmpty() || (email.contains("@") && email.contains("."))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    val isPasswordLengthValid: StateFlow<Boolean> = _password.map { password ->
        password.isEmpty() || password.length >= 8
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    init {
        viewModelScope.launch {
            dataStoreManager.userPreferencesFlow.collect { pair ->
                val savedEmail = pair.first
                val remember = pair.second
                isUserRemembered = remember && savedEmail != null
                if (isUserRemembered && savedEmail != null) {
                    rememberedUserName = savedEmail.substringBefore("@")
                }
            }
        }
    }

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
        clearValidationError()
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
        clearValidationError()
    }

    fun onCountrySelectionChanged(country: Country) {
        _selectedCountry.value = country
    }

    fun clearValidationError() {
        _validationError.value = null
    }

    fun clearRememberedUser() {
        viewModelScope.launch {
            dataStoreManager.clearAllPreferences()

            isUserRemembered = false
            rememberedUserName = null
            rememberMe = false
            _email.value = ""
            _password.value = ""
            _selectedCountry.value = Country.COUNTRY_A
        }
    }

    fun loginFirebase() {
        clearValidationError()
        if (!_email.value.contains("@") || _password.value.length < 8) {
            _validationError.value = "Por favor, ingrese un email válido y una contraseña de al menos 8 caracteres."
            _loginState.value = Resource.Idle()
            return
        }

        _loginState.value = Resource.Loading()
        val user = User(_email.value, _password.value)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val uid = loginUseCase(user)
                _loginState.value = Resource.Success(uid)

                if (rememberMe) {
                    onLoginSuccess(_email.value)
                }

            } catch (e: Exception) {
                val errorMessage = e.message ?: "Error desconocido al iniciar sesión."
                _loginState.value = Resource.Error(errorMessage)
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = Resource.Idle()
    }

    fun onLoginSuccess(userEmail: String) {
        viewModelScope.launch {
            dataStoreManager.saveUserCountry(userEmail, rememberMe, _selectedCountry.value)
        }
    }
}