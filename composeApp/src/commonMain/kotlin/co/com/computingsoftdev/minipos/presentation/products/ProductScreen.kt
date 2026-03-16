package co.com.computingsoftdev.minipos.presentation.products

import androidx.compose.foundation.background
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.com.computingsoftdev.minipos.domain.model.Product
import co.com.computingsoftdev.minipos.domain.model.SaleItem
import co.com.computingsoftdev.minipos.presentation.sales.SaleViewModel
import co.com.computingsoftdev.minipos.presentation.products.ProductUiState

@Composable
fun ProductScreen(
    productViewModel: ProductViewModel,
    saleViewModel: SaleViewModel
) {

    val uiState by productViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        productViewModel.loadProducts()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(uiState.products) { product ->
            ProductItem(
                product = product,
                onAddToSale = {
                    val saleItem = SaleItem(
                        product = it,  // tu Product
                        quantity = 1    // por ejemplo, 1 por defecto
                    )
                    saleViewModel.addItem(saleItem)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onAddToSale: (Product) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "Precio: ${product.price}", style = MaterialTheme.typography.bodyMedium)
            }
            Button(onClick = { onAddToSale(product) }) {
                Text("Agregar")
            }
        }
    }
}