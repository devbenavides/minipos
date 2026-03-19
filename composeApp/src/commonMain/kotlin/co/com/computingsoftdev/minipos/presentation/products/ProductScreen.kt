package co.com.computingsoftdev.minipos.presentation.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.com.computingsoftdev.minipos.domain.model.Product
import co.com.computingsoftdev.minipos.domain.model.SaleItem
import co.com.computingsoftdev.minipos.presentation.sales.SaleViewModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProductScreen(
    productViewModel: ProductViewModel,
    saleViewModel: SaleViewModel,
    onAddProductClick: () -> Unit,
    onEditProductClick: (Product) -> Unit,
    onAddProduct: (SaleItem) -> Unit
) {

    val uiState by productViewModel.uiState.collectAsState()
    var productToDelete by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(Unit) {
        productViewModel.loadProducts()
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // 👉 ESTE ES PARA IR AL FORM
        Button(
            onClick = onAddProductClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Nuevo Producto")
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(uiState.products) { product ->
                ProductItem(
                    product = product,
                    onAddToSale = { selectedProduct ->
                        val saleItem = SaleItem(
                            id = 0L,
                            saleId = saleViewModel.currentSale?.id ?: 0L,
                            productId = selectedProduct.id,
                            productName = selectedProduct.name,
                            price = selectedProduct.price,
                            quantity = 1
                        )
                        onAddProduct(saleItem) // <-- callback a SaleScreen
                    },
                    onEdit = {onEditProductClick(it)},
                    onDelete = {productToDelete = product}
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    if (productToDelete != null) {
        AlertDialog(
            onDismissRequest = { productToDelete = null },
            title = { Text("Eliminar producto") },
            text = { Text("¿Seguro que quieres eliminarlo?") },
            confirmButton = {
                Button(onClick = {
                    productViewModel.deleteProduct(productToDelete!!.id)
                    productToDelete = null
                }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    productToDelete = null
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ProductItem(
    product: Product,
    onAddToSale: (Product) -> Unit,
    onEdit: (Product) -> Unit,
    onDelete: (Long) -> Unit
) {
    var showDescription by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                    Text(text = "Precio: ${product.price}", style = MaterialTheme.typography.bodyMedium)
                }
                Row {
                    Button(onClick = { onAddToSale(product) }) {
                        Text("Agregar")
                    }
                    IconButton(onClick = { showDescription = true }) {
                        Text("ℹ️")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = { onEdit(product) }) {
                    Text("Editar")
                }
                TextButton(onClick = { onDelete(product.id) }) {
                    Text("Eliminar")
                }
            }
        }
    }

    // Dialogo de descripción
    if (showDescription) {
        AlertDialog(
            onDismissRequest = { showDescription = false },
            title = { Text("Descripción") },
            text = { Text(product.description ?: "Sin descripción") },
            confirmButton = {
                TextButton(onClick = { showDescription = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}