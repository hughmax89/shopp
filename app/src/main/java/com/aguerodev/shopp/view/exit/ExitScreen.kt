package com.aguerodev.shopp.view.shopping

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aguerodev.shopp.view.core.Login
import com.aguerodev.shopp.view.core.Resource

@Composable
fun ExitScreen(
    navController: NavController,
    viewModel: ExitViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val logoutState by viewModel.logoutState.collectAsState()

    LaunchedEffect(logoutState) {
        if (logoutState is Resource.Success) {
            navController.navigate(Login) {
                popUpTo(0) { inclusive = true }
            }
        }

        if (logoutState is Resource.Error) {
            val errorMessage = (logoutState as Resource.Error).message
            Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()

            viewModel.resetState()
        }
    }

    val isLoading = logoutState is Resource.Loading

    Surface(color = MaterialTheme.colorScheme.surface.copy(alpha = 0f)) {
        ExitConfirmationDialog(
            isLoading = isLoading,
            onCloseSession = {
                viewModel.clearDataAndLogout()
            },
            onExitApp = {
                activity?.finish()
            },
            onCancel = {
                navController.popBackStack()
            }
        )
    }
}
@Composable
fun ExitConfirmationDialog(
    isLoading: Boolean,
    onCloseSession: () -> Unit,
    onExitApp: () -> Unit,
    onCancel: () -> Unit
) {
    val isEnabled = !isLoading

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Confirmación de Salida") },
        text = {
            if (isLoading) {
                Text("Cerrando sesión y borrando datos...")
            } else {
                Text("Seleccione la acción deseada:")
            }
        },
        confirmButton = {
            TextButton(onClick = onCloseSession, enabled = isEnabled) {
                if (isLoading) {
                    CircularProgressIndicator(Modifier.size(24.dp))
                } else {
                    Text("CERRAR SESIÓN")
                }
            }
        },
        dismissButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 1. CANCELAR
                TextButton(onClick = onCancel, enabled = isEnabled) {
                    Text("CANCELAR")
                }
                // 2. SALIR DE LA APP
                TextButton(onClick = onExitApp, enabled = isEnabled) {
                    Text("SALIR")
                }
            }
        }
    )
}