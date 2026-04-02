package co.com.computingsoftdev.minipos.presentation.sales

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.com.computingsoftdev.minipos.domain.model.Product
import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleItem
import co.com.computingsoftdev.minipos.domain.model.SaleStatus
import co.com.computingsoftdev.minipos.presentation.products.ProductViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ui.buttons.IconButtonFilled
import ui.icons.AppIcons

@Composable
fun SaleScreen(
    saleViewModel: SaleViewModel,
    productViewModel: ProductViewModel
) {
    val saleUiState by saleViewModel.uiState.collectAsState()

    val currentSale = saleUiState.currentSale
    val cartItems = saleUiState.currentSale?.items ?: emptyList()
    val subtotal = saleUiState.subtotal
    val total = saleUiState.total
    val pendingSales = saleUiState.pendingSales

    var showProductDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val pendingSalesListState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(currentSale?.id, pendingSales) {
        val index = pendingSales.indexOfFirst { it.id == currentSale?.id }
        if (index >= 0) pendingSalesListState.animateScrollToItem(index)
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {

            // 🔹 Header
            Text(
                "Carrito de Venta",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 🔹 Carrito de venta (peso para ocupar espacio restante)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    if (cartItems.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No hay productos en el carrito", color = Color.Gray)
                            }
                        }
                    } else {
                        items(cartItems) { item ->
                            SaleItemRow(
                                item = item,
                                onQuantityChange = { newQty ->
                                    saleViewModel.updateItemQuantity(
                                        item.productId,
                                        newQty
                                    )
                                },
                                onRemove = { saleViewModel.removeItem(item.productId) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 Totales
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Subtotal: $subtotal")
                    Text("Total: $total", style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButtonFilled(
                    onClick = {showProductDialog = true},
                    icon = AppIcons.ListIcon,
                    text = "",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.weight(1f)
                )
                IconButtonFilled(
                    onClick = { saleViewModel.saveSale() },
                    icon = AppIcons.CheckIcon, // o el que tengas
                    text = "",
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    enabled = cartItems.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                )

                // 🔹 Nueva (neutral)
                IconButtonFilled(
                    onClick = { saleViewModel.startNewSale() },
                    icon = AppIcons.NotesIcon,
                    text = "",
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )

            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🔹 Ventas pendientes
            Text("Ventas Pendientes", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.55f) // ocupa un 30% del espacio disponible
            ) {
                if (pendingSales.isNotEmpty()) {
                    LazyColumn(state = pendingSalesListState) {
                        items(pendingSales) { sale ->
                            PendingSaleRow(
                                sale = sale,
                                isSelected = sale.id == currentSale?.id,
                                onSelect = { saleViewModel.selectPendingSale(sale) },
                                onDelete = { saleViewModel.cancelSale(sale) }
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay ventas pendientes", color = Color.Gray)
                    }
                }
            }
        }
    }

    // 🔹 Diálogo de selección de productos
    if (showProductDialog) {
        ProductSelectionDialog(
            products = productViewModel.uiState.value.products,
            onSelect = { product ->
                currentSale?.let { sale ->
                    saleViewModel.addItem(
                        SaleItem(
                            id = 0L,
                            saleId = sale.id,
                            productId = product.id,
                            productName = product.name,
                            price = product.price,
                            quantity = 1
                        )
                    )

                    val productName = product.name
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "$productName agregado al carrito",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            },
            onDismiss = { showProductDialog = false }
        )
    }
}

// Composable para un item del carrito
@Composable
fun SaleItemRow(
    item: SaleItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.productName,
            modifier = Modifier.weight(1f), // ocupa espacio disponible
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (item.quantity > 1) onQuantityChange(item.quantity - 1) }) {
                Text("-")
            }
            Text("${item.quantity}")
            IconButton(onClick = { onQuantityChange(item.quantity + 1) }) {
                Text("+")
            }
        }
        // 🔹 Subtotal
        Text(
            text = "$${item.subtotal()}",
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        // 🔹 Eliminar (SIEMPRE visible 🔥)
        IconButton(onClick = onRemove) {
            Text("🗑️")
        }
    }
}

// Composable para mostrar ventas pendientes
@Composable
fun PendingSaleRow(
    sale: Sale,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDelete: (Sale) -> Unit
) {
    // 🔥 Recalcular total en memoria para mostrar en listado
    val total = sale.items.sumOf { it.subtotal() }
    val isEmpty = sale.items.isEmpty()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Venta #${sale.id}")
                Text("Items: ${sale.items.size}")
                Text("Total: $total")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón único dinámico
            Button(
                onClick = { onDelete(sale) },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(160.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isEmpty)
                        MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                    else
                        MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isEmpty)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Icon(
                    imageVector = if (isEmpty) AppIcons.TrashIcon else AppIcons.XIcon,
                    contentDescription = if (isEmpty) "Eliminar" else "Cancelar",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isEmpty) "Eliminar" else "Cancelar")
            }
        }
    }
}

// Composable simple para seleccionar productos
@Composable
fun ProductSelectionDialog(
    products: List<Product>,
    onSelect: (Product) -> Unit,
    onDismiss: () -> Unit
) {
    var addedCount by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Selecciona un producto")
                if (addedCount > 0) {
                    Text(
                        "$addedCount producto(s) agregado(s)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                }
            }
        },
        text = {
            LazyColumn {
                items(products) { product ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(product.name)
                            Text("Precio: ${product.price}")
                        }
                        Button(
                            onClick = {
                                onSelect(product)
                                addedCount++
                            }
                        ) {
                            Text("Agregar")
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cerrar") }
        }
    )
}
