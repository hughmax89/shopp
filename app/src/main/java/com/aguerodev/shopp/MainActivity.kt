package com.aguerodev.shopp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.aguerodev.shopp.view.core.NavigationWrapper
import com.aguerodev.shopp.view.ui.theme.ShoppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var biometricManager: BiometricManager
    private lateinit var biometricPrompt: BiometricPrompt
    private var onBiometricSuccess: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        biometricManager = BiometricManager.from(this)
        setupBiometricPrompt()

        enableEdgeToEdge()
        setContent {
            ShoppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationWrapper(
                        modifier = Modifier,
                        onBiometricLogin = { onSuccessCallback ->
                            onBiometricSuccess = onSuccessCallback
                            showBiometricAuth()
                        },
                        isBiometricReady = isBiometricAvailable()
                    )
                }
            }
        }
    }
    private fun setupBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(
            this, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        this@MainActivity,
                        "Autenticación biométrica exitosa",
                        Toast.LENGTH_SHORT
                    ).show()
                    onBiometricSuccess?.invoke()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        this@MainActivity,
                        "Error de Biometría: $errString",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }
    private fun showBiometricAuth() {
        val canAuthenticate =
            biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)

        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Inicio de sesión con Biometría")
                .setSubtitle("Usa tu huella para acceder")
                .setNegativeButtonText("Usar Contraseña / Cancelar")
                .build()
            biometricPrompt.authenticate(promptInfo)
        } else {
            Toast.makeText(this, "Biometría no disponible en este dispositivo.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun isBiometricAvailable(): Boolean {
        val canAuthenticate = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        return canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS
    }
}
