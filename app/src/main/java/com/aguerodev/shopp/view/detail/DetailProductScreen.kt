package com.aguerodev.shopp.view.detail

import android.content.pm.ActivityInfo
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.aguerodev.shopp.domain.entity.Product
import com.aguerodev.shopp.view.core.DetailNavigationState
import com.aguerodev.shopp.view.core.Resource
import com.aguerodev.shopp.view.home.getCountryTheme
import com.aguerodev.shopp.view.util.CreditCardVisualTransformation
import com.aguerodev.shopp.view.util.ExpiryDateValid
import com.aguerodev.shopp.view.util.findActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailProductScreen(
    productId: Int,
    viewModel: DetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onPurchaseComplete: () -> Unit
) {
    val activity = LocalContext.current.findActivity()
    DisposableEffect(Unit) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
    val productState by viewModel.productState.collectAsState()
    val selectedCountry by viewModel.selectedCountry.collectAsState()
    val navigationState by viewModel.navigationState.collectAsState()

    val countryTheme = getCountryTheme(selectedCountry)
    val containerColor = countryTheme.containerColor

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = productState.data?.title ?: "Detalle"
                    Text(title)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (productState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(
                        color = containerColor,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is Resource.Success -> {
                    val product = productState.data!!

                    DetailContent(
                        product = product,
                        containerColor = containerColor,
                        onBuyClicked = { viewModel.setNavigationState(DetailNavigationState.PURCHASE) }
                    )
                }

                is Resource.Error -> {
                    val message = productState.message ?: "Error desconocido al cargar."
                    Text(
                        text = message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is Resource.Idle -> { /* Estado inicial */
                }
            }

            when (navigationState) {
                DetailNavigationState.PURCHASE -> {
                    productState.data?.let { product ->
                        PurchaseModal(
                            product = product,
                            onClose = { viewModel.setNavigationState(DetailNavigationState.DETAIL) },
                            onPurchaseConfirmed = {
                                viewModel.markProductAsSold(product.id)
                                viewModel.setNavigationState(DetailNavigationState.SUCCESS)
                            }
                        )
                    }
                }

                DetailNavigationState.SUCCESS -> {
                    SuccessScreen(onAccept = onPurchaseComplete)

                }

                DetailNavigationState.DETAIL -> { /* No se muestra modal */
                }
            }
        }
    }
}

@Composable
fun DetailContent(product: Product, containerColor: Color, onBuyClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ImageCarousel(product = product)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$${String.format("%.2f", product.price)}",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold
            )
            product.rating.let { rating ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format("%.1f", rating),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = product.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Categoría: ${product.categoryName}",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onBuyClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "COMPRAR AHORA",
                color = Color.White,
                fontWeight = FontWeight.Black,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun ImageCarousel(product: Product) {
    val starPainter = rememberVectorPainter(Icons.Default.Star)
    val currentImageIndex = remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    val imageUrls = product.imageUrls
    val hasMultipleImages = imageUrls.size > 1
    val currentImageUrl = imageUrls.getOrNull(currentImageIndex.value)

    if (hasMultipleImages) {
        LaunchedEffect(imageUrls) {
            scope.launch {
                while (true) {
                    delay(3000L)
                    currentImageIndex.value = (currentImageIndex.value + 1) % imageUrls.size
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        if (currentImageUrl.isNullOrEmpty()) {
            Icon(
                painter = starPainter,
                contentDescription = "No hay imagen",
                tint = Color.Gray,
                modifier = Modifier.size(64.dp)
            )
        } else {
            Crossfade(targetState = currentImageUrl, label = "ImageTransition") { url ->
                AsyncImage(
                    model = url,
                    contentDescription = product.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    error = starPainter
                )
            }
        }

        if (hasMultipleImages) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                imageUrls.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (index == currentImageIndex.value) Color.White else Color.White.copy(
                                    alpha = 0.5f
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun PurchaseModal(
    product: Product,
    onClose: () -> Unit,
    onPurchaseConfirmed: () -> Unit
) {
    Dialog(onDismissRequest = onClose) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            color = MaterialTheme.colorScheme.surfaceContainerHigh
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Cerrar",
                        modifier = Modifier.clickable(onClick = onClose)
                    )
                }

                Text(
                    "Confirmar Compra",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Divider(modifier = Modifier.padding(bottom = 8.dp))

                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "$${String.format("%.2f", product.price)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                PaymentForm(product, onPurchaseConfirmed)
            }
        }
    }
}

@Composable
fun PaymentForm(product: Product, onPurchaseConfirmed: () -> Unit) {
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Modalidad de Pago",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = cardNumber,
            onValueChange = { newValue ->
                val cleanedValue = newValue.filter { it.isDigit() }

                if (cleanedValue.length <= 16) {
                    cardNumber = cleanedValue
                }
            },
            label = { Text("Número de Tarjeta") },

            visualTransformation = CreditCardVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = expiryDate,
                onValueChange = { newValue ->
                    var filteredValue = newValue.filter { it.isDigit() }
                    if (filteredValue.length > 2) {
                        filteredValue = filteredValue.substring(0, 2) + "/" + filteredValue.substring(2)
                    }

                    if (filteredValue.length <= 5) {
                        expiryDate = filteredValue
                    }
                },
                label = { Text("MM/YY") },
                colors = if (expiryDate.isNotEmpty() && !ExpiryDateValid(expiryDate)) {
                    OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Red,
                        focusedBorderColor = Color.Red
                    )
                } else {
                    OutlinedTextFieldDefaults.colors()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = cvv,
                onValueChange = { if (it.length <= 3) cvv = it },
                label = { Text("CVV") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onPurchaseConfirmed,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = cardNumber.length == 16 && expiryDate.length == 5 && cvv.length == 3,
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Completar Compra ($${String.format("%.2f", product.price)})", color = Color.White)
        }
    }
}

@Composable
fun SuccessScreen(onAccept: () -> Unit) {
    Dialog(onDismissRequest = onAccept) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF4CAF50)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Éxito",
                    tint = Color.White,
                    modifier = Modifier.size(96.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "¡Compra Realizada con Éxito!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(48.dp))
                Button(
                    onClick = onAccept,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Aceptar",
                        color = Color(0xFF4CAF50),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}