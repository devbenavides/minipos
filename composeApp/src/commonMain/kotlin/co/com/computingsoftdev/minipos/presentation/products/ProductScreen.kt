package co.com.computingsoftdev.minipos.presentation.products

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import ui.buttons.IconButtonFilled
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

        //ESTE ES PARA IR AL FORM
        Button(
            onClick = onAddProductClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = AppIcons.PlusIcon, // tu icono de más
                contentDescription = "Nuevo Producto",
                modifier = Modifier.size(28.dp),
                tint = Color.White
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
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .animateContentSize() // 🔥 animación suave
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1
                    )

                    Text(
                        text = "Precio: ${product.price}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // 🔹 Solo si tiene descripción
                if (!product.description.isNullOrBlank()) {
                    IconButton(
                        onClick = { expanded = !expanded }
                    ) {
                        Icon(
                            imageVector = if (expanded)
                                AppIcons.ChevronUpIcon
                            else
                                AppIcons.ChevronDownIcon,
                            contentDescription = "Descripción",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // 🔹 Descripción expandible
            if (expanded && !product.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = product.description ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            ProductCardButtons(
                onEdit = { onEdit(product) },
                onDelete = { onDelete(product.id) }
            )
        }
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
        //Editar
        Button(
            onClick = onEdit,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = AppIcons.PencilIcon,
                contentDescription = "Editar",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Editar")
        }

        //Eliminar
        Button(
            onClick = onDelete,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                contentColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
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