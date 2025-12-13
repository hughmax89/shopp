package com.aguerodev.shopp.view.login

import android.content.pm.ActivityInfo
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aguerodev.shopp.view.util.findActivity


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onBiometricLoginRequest: () -> Unit
) {
    val activity = LocalContext.current.findActivity()

    LaunchedEffect(Unit) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    if (viewModel.isUserRemembered) {
        BiometricWelcomeScreen(
            userName = viewModel.rememberedUserName ?: "Usuario",
            onAuthenticate = { onBiometricLoginRequest() },
            onUseOtherAccount = { viewModel.isUserRemembered = false }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF192A56))
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bienvenido",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 48.dp, bottom = 32.dp)
        )

        CustomTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = "Usuario (Email)",
            isError = !viewModel.isEmailValid && viewModel.email.isNotEmpty(),
            errorMessage = "Debe ser un email válido (ej: user@mail.com)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(Modifier.height(16.dp))

        CustomTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = "Contraseña",
            isError = !viewModel.isPasswordValid && viewModel.password.isNotEmpty(),
            errorMessage = "Mínimo 8 caracteres",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(24.dp))

        AnimatedCountrySelector(
            selectedCountry = viewModel.selectedCountry,
            onCountrySelected = { country -> viewModel.selectedCountry = country }
        )
        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = viewModel.rememberMe,
                onCheckedChange = { viewModel.rememberMe = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = Color.White
                )
            )
            Text("Recordar mi usuario y usar Biometría", color = Color.White.copy(alpha = 0.8f))
        }
        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                keyboardController?.hide()
                viewModel.attemptLocalLogin()
                if (viewModel.isFormValid) {
                    onLoginSuccess()
                }
            },
            enabled = viewModel.isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("ACCEDER", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                keyboardController?.hide()
                viewModel.attemptFirebaseLogin()
                if (viewModel.isFormValid) {
                    onLoginSuccess()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            border = BorderStroke(1.dp, Color.White),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text("Login con Firebase", fontSize = 16.sp)
        }

        viewModel.loginError?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun BiometricWelcomeScreen(
    userName: String,
    onAuthenticate: () -> Unit,
    onUseOtherAccount: () -> Unit
) {
    // Usamos el color de fondo oscuro que definimos para el login
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF192A56))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Bienvenido de nuevo,",
                color = Color.White,
                fontSize = 24.sp
            )
            Text(
                // Muestra el nombre del usuario
                text = userName,
                color = MaterialTheme.colorScheme.primary, // Color de acento
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Sección del sensor biométrico
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Filled.Fingerprint,
                contentDescription = "Icono de Biometría",
                tint = Color.White,
                modifier = Modifier
                    .size(100.dp)
                    // Al hacer clic, se dispara la solicitud de autenticación
                    .clickable { onAuthenticate() }
            )
            Text(
                text = "Toca el icono para Iniciar Sesión con tu huella",
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 16.dp),
                fontSize = 16.sp
            )
        }

        // Opción para usar una cuenta diferente
        TextButton(onClick = onUseOtherAccount) {
            Text(
                "Usar otra cuenta o contraseña",
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun CustomTextField(
    // 💡 SOLUCIÓN 2: La lambda DEBE aceptar el nuevo String escrito
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    errorMessage: String,
    keyboardOptions: KeyboardOptions,
    // Opcional: Agregamos parámetros para hacerlo más flexible (ej. Contraseña)
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    // Usamos OutlinedTextField, que es común en Material Design
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        supportingText = if (isError) {
            { Text(errorMessage) }
        } else {
            null
        },
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun AnimatedCountrySelector(selectedCountry: String, onCountrySelected: (String) -> Unit) {
    val isBrazil = selectedCountry == "País B"

    // 1. Definición de colores
    val argentinaColor = Color(0xFF74ACDF) // Azul cielo de Argentina
    val brazilColor = Color(0xFFFDE747)    // Amarillo de Brasil

    // 2. Animación del Color de Fondo
    val backgroundColor by animateColorAsState(
        targetValue = if (isBrazil) brazilColor else argentinaColor,
        animationSpec = tween(durationMillis = 500)
    )

    // 3. Animación del Color del Texto
    val contentColor by animateColorAsState(
        targetValue = if (isBrazil) Color.Black else Color.White,
        animationSpec = tween(durationMillis = 500)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable { /* Abre el diálogo o desplegable */ },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Selecciona tu País:",
                color = contentColor,
                modifier = Modifier.padding(start = 16.dp)
            )

            // Usamos un Dropdown para la selección
            var expanded by remember { mutableStateOf(false) }
            TextButton(
                onClick = { expanded = true },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(
                    text = selectedCountry,
                    color = contentColor,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    Icons.Filled.ArrowDropDown,
                    contentDescription = "Seleccionar país",
                    tint = contentColor
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("País A (Argentina)") },
                    onClick = {
                        onCountrySelected("País A")
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("País B (Brasil)") },
                    onClick = {
                        onCountrySelected("País B")
                        expanded = false
                    }
                )
            }
        }
    }
}