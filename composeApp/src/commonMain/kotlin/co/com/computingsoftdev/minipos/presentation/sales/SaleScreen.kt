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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.com.computingsoftdev.minipos.domain.model.Product
import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleItem
import co.com.computingsoftdev.minipos.presentation.components.SaleListComponent
import co.com.computingsoftdev.minipos.presentation.products.ProductViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun SaleScreen(
    saleViewModel: SaleViewModel,
    productViewModel: ProductViewModel
) {
    val currentSale = saleViewModel.currentSale
    val cartItems = currentSale?.items ?: emptyList()
    val subtotal = saleViewModel.subtotal
    val total = saleViewModel.total
    val pendingSales = saleViewModel.pendingSales

    // Estado para mostrar diálogo de selección de productos
    var showProductDialog by remember { mutableStateOf(false) }
    val pendingSalesListState = rememberLazyListState()

    LaunchedEffect(currentSale?.id, pendingSales) {
        val index = pendingSales.indexOfFirst { it.id == currentSale?.id }
        if (index >= 0) {
            pendingSalesListState.animateScrollToItem(index)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // 🔹 Header
        Text(
            "Carrito de Venta",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 Carrito (ocupa el espacio principal)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(cartItems) { item ->
                SaleItemRow(
                    item = item,
                    onQuantityChange = { newQty ->
                        saleViewModel.updateItemQuantity(item.productId, newQty)
                    },
                    onRemove = { saleViewModel.removeItem(item.productId) }
                )
            }
        }

        // 🔹 Totales (fijos)
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

        // 🔹 Botones compactos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { showProductDialog = true },
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 6.dp)
            ) {
                Text("Agregar")
            }

            Button(
                onClick = { saleViewModel.saveSale() },
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 6.dp)
            ) {
                Text("Finalizar")
            }

            Button(
                onClick = { saleViewModel.startNewSale() },
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 6.dp)
            ) {
                Text("Nueva")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 Ventas pendientes (ALTURA CONTROLADA 🔥)
        if (pendingSales.isNotEmpty()) {

            Text(
                "Ventas Pendientes",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp) // 🔥 CLAVE: evita que tape el carrito
            ) {
                items(pendingSales) { sale ->
                    PendingSaleRow(
                        sale = sale,
                        isSelected = sale.id == currentSale?.id,
                        //onSelect = { saleViewModel.loadSale(sale.id) },
                        onSelect = { saleViewModel.selectPendingSale(sale) },
                        onDelete = {saleViewModel.deletePendingSale(sale)}
                    )
                }
            }
        }
    }

    // Dialogo para seleccionar producto
    if (showProductDialog) {
        ProductSelectionDialog(
            products = productViewModel.uiState.value.products,
            onSelect = { product ->
                saleViewModel.addItem(
                    SaleItem(
                        id = 0L,
                        saleId = currentSale?.id ?: 0L,
                        productId = product.id,
                        productName = product.name,
                        price = product.price,
                        quantity = 1
                    )
                )
                showProductDialog = false
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
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Venta #${sale.id}")
            Text("Items: ${sale.items.size}")
            Text("Total: ${sale.total}")
        }
        // Botón eliminar
        IconButton(
            onClick = { onDelete(sale) },
            enabled = sale.items.isEmpty() // solo se puede eliminar si no hay items
        ) {
            Text("🗑️")
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
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Selecciona un producto") },
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
                        Button(onClick = { onSelect(product) }) {
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
