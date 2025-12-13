package com.aguerodev.shopp.view.login


import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aguerodev.shopp.view.util.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val dataStoreManager: DataStoreManager
) : AndroidViewModel(application) {

    // Estados Mutables
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var selectedCountry by mutableStateOf("País A")
    var rememberMe by mutableStateOf(false)
    var loginError by mutableStateOf<String?>(null)
    var isUserRemembered by mutableStateOf(false)
    var rememberedUserName by mutableStateOf<String?>(null)

    // Estados de Validación
    val isEmailValid: Boolean
        get() = email.contains("@") && email.length > 5
    val isPasswordValid: Boolean
        get() = password.length >= 8
    val isFormValid: Boolean
        get() = isEmailValid && isPasswordValid

    init {
        // Carga los datos guardados al iniciar
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

    // --- Lógica de persistencia ---
    fun onLoginSuccess(userEmail: String) {
        viewModelScope.launch {
            dataStoreManager.saveUserEmail(userEmail, rememberMe)
        }
        // Navegación (implementada en el Composable)
    }

    // --- Lógica de Autenticación Local ---
    fun attemptLocalLogin() {
        if (!isFormValid) {
            loginError = "Por favor, revisa tus credenciales y el país seleccionado."
            return
        }
        // Lógica de validación con credenciales hardcodeadas (ejemplo)
        if (email == "test@test.com" && password == "password123") {
            onLoginSuccess(email)
        } else {
            loginError = "Credenciales incorrectas o país no seleccionado."
        }
    }

    // --- Lógica de Autenticación Firebase (Simulación) ---
    fun attemptFirebaseLogin() {
        // Implementación real con Firebase Auth:
        /* FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onLoginSuccess(email) // O el email de Firebase
                } else {
                    loginError = task.exception?.message ?: "Error de autenticación con Firebase."
                }
            }
        */
        // Simulación de éxito para el ejemplo:
        if (isFormValid) {
            onLoginSuccess(email)
        } else {
            loginError = "Error simulado en Firebase: revise el formulario."
        }
    }
}