package co.com.computingsoftdev.minipos.presentation.sales

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.com.computingsoftdev.minipos.domain.model.SaleItem
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
@OptIn(ExperimentalTime::class)
fun SaleScreen(viewModel: SaleViewModel) {

    val cartItems = viewModel.cartItems
    val subtotal = viewModel.subtotal
    val total = viewModel.total

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Carrito", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(cartItems) { item ->
                SaleItemRow(item)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Subtotal: $subtotal")
        Text("Total: $total")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.saveSale(Clock.System.now().toEpochMilliseconds())
            }
        ) {
            Text("Guardar Venta")
        }
    }
}

@Composable
fun SaleItemRow(item: SaleItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(item.product.name)
        Text("Qty: ${item.quantity}")
        Text("Subtotal: ${item.subtotal()}")
    }
}