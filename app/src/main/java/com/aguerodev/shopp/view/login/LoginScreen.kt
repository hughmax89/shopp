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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.aguerodev.shopp.domain.entity.Country
import com.aguerodev.shopp.view.core.Resource
import com.aguerodev.shopp.view.util.findActivity


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onBiometricLoginRequest: () -> Unit,
    isBiometricAvailable: Boolean
) {
    val activity = LocalContext.current.findActivity()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val selectedCountryValue by viewModel.selectedCountry.collectAsState()
    val loginState by viewModel.loginState.collectAsState()
    val emailValue by viewModel.email.collectAsState()
    val passwordValue by viewModel.password.collectAsState()
    val isEmailValid by viewModel.isEmailFormatValid.collectAsState()
    val isPasswordValid by viewModel.isPasswordLengthValid.collectAsState()
    val validationErrorMessage by viewModel.validationError.collectAsState()
    val isLoggingIn = loginState is Resource.Loading
    val keyboardController = LocalSoftwareKeyboardController.current

    val argentinaBlue = Color(0xFF74ACDF)
    val brazilYellow = Color(0xFFFDE747)
    val brazilGreen = Color(0xFF009739)
    val isBrazil = selectedCountryValue == Country.COUNTRY_B

    val buttonColor by animateColorAsState(
        targetValue = if (isBrazil) brazilYellow else argentinaBlue,
        animationSpec = tween(500), label = "ButtonColor"
    )

    val primaryCountryColor by animateColorAsState(
        targetValue = if (isBrazil) brazilGreen else argentinaBlue,
        animationSpec = tween(500), label = "CountryPrimaryColor"
    )

    val contentColor = if (isBrazil) Color.Black else Color.White
    val loadingColor = Color.White

    LaunchedEffect(Unit) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
    LaunchedEffect(loginState) {
        when (loginState) {
            is Resource.Success -> {
                onLoginSuccess()
                viewModel.resetLoginState()
            }
            else -> Unit
        }
    }

    if (loginState is Resource.Error) {
        val errorState = loginState as Resource.Error

        LoginErrorModal(
            errorMessage = errorState.message ?: "Error al autenticar. Inténtelo de nuevo.",
            onDismiss = viewModel::resetLoginState,
            viewModel = viewModel
        )
    }

    if (viewModel.isUserRemembered) {
        if (isBiometricAvailable) {
            BiometricWelcomeScreen(
                userName = viewModel.rememberedUserName ?: "Usuario",
                primaryCountryColor = primaryCountryColor,
                onAuthenticate = { onBiometricLoginRequest() },
                onUseOtherAccount = { viewModel.clearRememberedUser() }
            )
        } else {
            RememberedUserScreen(
                userName = viewModel.rememberedUserName ?: "Usuario",
                onContinue = {
                    onLoginSuccess()
                },
                onUseOtherAccount = { viewModel.clearRememberedUser() }
            )
        }
        return
    }

    validationErrorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = viewModel::clearValidationError,
            title = {
                Text("Error de validación ⚠️")
            },
            text = {
                Text(message)
            },
            confirmButton = {
                Button(
                    onClick = viewModel::clearValidationError
                ) {
                    Text("Entendido")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bienvenido",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 48.dp)
        )

        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Icono de carrito de compras",
            tint = primaryCountryColor,
            modifier = Modifier
                .padding(bottom = 32.dp, top = 8.dp)
                .size(48.dp)
        )

        CustomTextField(
            value = emailValue,
            onValueChange = viewModel::onEmailChanged,
            label = "Usuario",
            isError = !isEmailValid && emailValue.isNotEmpty(),
            errorMessage = "Debe ser un email válido (ej: user@mail.com)",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(Modifier.height(16.dp))

        CustomTextField(
            value = passwordValue,
            onValueChange = viewModel::onPasswordChanged,
            label = "Contraseña",
            isError = !isPasswordValid && passwordValue.isNotEmpty(),
            errorMessage = "Mínimo 8 caracteres",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),

            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else
                    Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description, tint = Color.White)
                }
            }
        )
        Spacer(Modifier.height(24.dp))

        CountrySwitch(
            currentCountry = selectedCountryValue,
            onCountryChange = viewModel::onCountrySelectionChanged
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
            Text(
                text = if (isBiometricAvailable) "Recordar mi usuario y usar Biometría" else "Recordar usuario",
                color = Color.White.copy(alpha = 0.8f)
            )
        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                keyboardController?.hide()
                viewModel.loginFirebase()
            },
            enabled = !isLoggingIn,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),

            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = buttonColor,
                contentColor = contentColor,
                disabledContainerColor = buttonColor.copy(alpha = 0.6f)
            ),
            border = BorderStroke(1.dp, contentColor)
        ) {
            if (isLoggingIn) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = loadingColor,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Login", fontSize = 16.sp)
            }
        }
    }
}


@Composable
fun BiometricWelcomeScreen(
    userName: String,
    primaryCountryColor: Color, // 💡 Nuevo parámetro
    onAuthenticate: () -> Unit,
    onUseOtherAccount: () -> Unit
) {
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
                text = userName,
                color = primaryCountryColor, // 💡 Nombre con color del país
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Filled.Fingerprint,
                contentDescription = "Icono de Biometría",
                tint = Color.White,
                modifier = Modifier
                    .size(100.dp)
                    .clickable { onAuthenticate() }
            )
            Text(
                text = "Toca el icono para Iniciar Sesión con tu huella",
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 16.dp),
                fontSize = 16.sp
            )
        }
        TextButton(onClick = onUseOtherAccount) {
            Text(
                "Usar otra cuenta o contraseña",
                color = primaryCountryColor, // 💡 Botón con color del país
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    errorMessage: String,
    keyboardOptions: KeyboardOptions,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray) },
        isError = isError,
        supportingText = if (isError) {
            { Text(errorMessage, color = MaterialTheme.colorScheme.error) }
        } else {
            null
        },
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Gray,
            cursorColor = Color.White
        )
    )
}

@Composable
fun CountrySwitch(
    currentCountry: Country,
    onCountryChange: (Country) -> Unit
) {
    val argentinaBlue = Color(0xFF74ACDF)
    val argentinaWhite = Color.White
    val brazilYellow = Color(0xFFFDE747)
    val brazilGreen = Color(0xFF009739)

    val checkedState = currentCountry == Country.COUNTRY_B
    val animatedTrackColor by animateColorAsState(
        targetValue = if (checkedState) brazilGreen else argentinaBlue,
        animationSpec = tween(500), label = "TrackColor"
    )
    val animatedThumbColor by animateColorAsState(
        targetValue = if (checkedState) brazilYellow else argentinaWhite,
        animationSpec = tween(500), label = "ThumbColor"
    )

    Row(
        modifier = Modifier
            .width(280.dp)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Argentina",
            color = if (checkedState) Color.Gray else argentinaBlue
        )

        Switch(
            checked = checkedState,
            onCheckedChange = { isChecked ->
                val newCountry = if (isChecked) Country.COUNTRY_B else Country.COUNTRY_A
                onCountryChange(newCountry)
            },
            colors = SwitchDefaults.colors(
                checkedTrackColor = animatedTrackColor,
                uncheckedTrackColor = animatedTrackColor,
                checkedThumbColor = animatedThumbColor,
                uncheckedThumbColor = animatedThumbColor,
                disabledCheckedThumbColor = Color.Gray,
                disabledUncheckedThumbColor = Color.LightGray
            ),
            modifier = Modifier.size(width = 80.dp, height = 48.dp)
        )

        Text(
            text = "Brasil",
            color = if (checkedState) brazilGreen else Color.Gray
        )
    }
}

@Composable
fun LoginErrorModal(
    errorMessage: String,
    onDismiss: () -> Unit,
    viewModel: LoginViewModel
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Error de Acceso 🚨")
        },
        text = {
            Text(errorMessage, color = Color.Black)
        },
        confirmButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Aceptar")
            }
        }
    )
}

@Composable
fun RememberedUserScreen(
    userName: String,
    onContinue: () -> Unit,
    onUseOtherAccount: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
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
                text = userName,
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
            )
        ) {
            Text(
                "ENTRAR",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        TextButton(onClick = onUseOtherAccount) {
            Text(
                "Usar otra cuenta y/o País",
                color = Color.White.copy(alpha = 0.8f),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}