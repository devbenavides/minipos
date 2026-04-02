package co.com.computingsoftdev.minipos.presentation.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.com.computingsoftdev.minipos.domain.model.Product
import co.com.computingsoftdev.minipos.domain.model.SaleItem
import co.com.computingsoftdev.minipos.presentation.sales.SaleViewModel
import org.jetbrains.compose.resources.painterResource
import ui.icons.AppIcons
import ui.navigation.NavBarIcon

@Composable
fun ProductScreen(
    productViewModel: ProductViewModel,
    saleViewModel: SaleViewModel,
    onAddProductClick: () -> Unit,
    onEditProductClick: (Product) -> Unit,
    onAddProduct: (SaleItem) -> Unit
) {

    val saleUiState by saleViewModel.uiState.collectAsState()
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
            NavBarIcon(
                AppIcons.PlusIcon,
                "Nuevo Producto",
                iconSize = 28.dp,
                iconColor = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
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
                            saleId = saleUiState.currentSale?.id ?: 0L,
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
                    /*Button(onClick = { onAddToSale(product) }) {
                        NavBarIcon(
                            AppIcons.PlusIcon,
                            "Agregar",
                            iconSize = 28.dp,
                            iconColor = Color.Gray
                        )
                        Text("Agregar")
                    }*/
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        IconButton(
                            onClick = { showDescription = true },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = AppIcons.InfoCircleIcon,
                                contentDescription = "Ver descripción",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ProductCardButtons(
                onEdit = { onEdit(product) },
                onDelete = { onDelete(product.id) }
            )
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

@Composable
fun ProductCardButtons(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 🔹 Editar
        Button(
            onClick = onEdit,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = AppIcons.PencilIcon,
                contentDescription = "Editar",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Editar")
        }

        // 🔹 Eliminar
        Button(
            onClick = onDelete,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                contentColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = AppIcons.TrashIcon,
                contentDescription = "Eliminar",
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Eliminar")
        }
    }
}