package com.aguerodev.shopp.view.shopping

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.aguerodev.shopp.domain.entity.Product
import com.aguerodev.shopp.view.core.Resource
import kotlin.math.absoluteValue

// Dimensiones clave para el carrusel
private val ITEM_WIDTH = 350.dp // Ancho base de la tarjeta
private val ITEM_HEIGHT = 600.dp // Alto base de la tarjeta
private const val SCALE_MAX = 1.0f // Escala del item centrado
private const val SCALE_MIN = 0.8f // Escala mínima de los items laterales
private const val ALPHA_MIN = 0.5f // Opacidad mínima de los items laterales

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val historyState by viewModel.purchaseHistoryState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Shopping", fontWeight = FontWeight.Bold) })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            when (historyState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is Resource.Error -> {
                    Text((historyState as Resource.Error).message ?: "Error al cargar el historial", color = MaterialTheme.colorScheme.error)
                }
                is Resource.Success -> {
                    val products = historyState.data!!

                    if (products.isEmpty()) {
                        EmptyHistoryView()
                    } else {
                        ProductHistoryCarousel(products = products)
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
fun ProductHistoryCarousel(products: List<Product>) {
    val listState = rememberLazyListState()
    val density = LocalDensity.current

    val visibleItemsInfo by remember { derivedStateOf { listState.layoutInfo.visibleItemsInfo } }

    val containerWidthPx = with(density) { (ITEM_WIDTH + 32.dp).toPx() }
    val halfScreenWidth = with(density) { LocalContext.current.resources.displayMetrics.widthPixels / 2f }

    val focusedItemIndex by remember {
        derivedStateOf {
            visibleItemsInfo
                .minByOrNull { info -> (info.offset + info.size / 2f - halfScreenWidth).absoluteValue }
                ?.index ?: -1
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyRow(
            state = listState,
            contentPadding = PaddingValues(horizontal = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(ITEM_HEIGHT + 32.dp)
        ) {
            itemsIndexed(products) { index, product ->
                val centerOffset = visibleItemsInfo.firstOrNull { it.index == index }?.let { info ->
                    (info.offset + info.size / 2f) - halfScreenWidth
                } ?: Float.MAX_VALUE
                val scaleFactor = 1f - (centerOffset.absoluteValue / containerWidthPx).coerceAtMost(1f) * (SCALE_MAX - SCALE_MIN)
                val alphaFactor = 1f - (centerOffset.absoluteValue / containerWidthPx).coerceAtMost(1f) * (SCALE_MAX - ALPHA_MIN)

                ProductCardFocused(
                    product = product,
                    scale = scaleFactor.coerceIn(SCALE_MIN, SCALE_MAX),
                    alpha = alphaFactor.coerceIn(ALPHA_MIN, SCALE_MAX)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (products.isNotEmpty()) {
            Text(
                text = "${focusedItemIndex + 1} / ${products.size}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ProductCardFocused(product: Product, scale: Float, alpha: Float) {
    val infoPainter = rememberVectorPainter(Icons.Default.Info)

    Card(
        modifier = Modifier
            .width(ITEM_WIDTH)
            .height(ITEM_HEIGHT)
            .scale(scale)
            .alpha(alpha),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp * scale.coerceAtLeast(0.5f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.LightGray)
            ) {
                AsyncImage(
                    model = product.imageUrls.firstOrNull(),
                    contentDescription = product.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    error = infoPainter
                )
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.categoryName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "$${String.format("%.2f", product.price)}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
fun EmptyHistoryView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Info,
            contentDescription = "Historial vacío",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Aún no has realizado compras.",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Explora nuestros productos y haz tu primera compra para verla aquí.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}