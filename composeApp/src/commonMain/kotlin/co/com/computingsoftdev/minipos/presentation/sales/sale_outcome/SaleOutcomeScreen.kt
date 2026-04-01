package co.com.computingsoftdev.minipos.presentation.sales.sale_outcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleStatus

@Composable
fun SaleOutcomeScreen(
    saleOutcomeViewModel: SaleOutcomeViewModel
) {
    val sales = saleOutcomeViewModel.completedSales
    val salesListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // 🔹 Título
        Text(
            "Ventas Finalizadas",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 Filtros
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { saleOutcomeViewModel.applyFilter(null) }) {
                Text("Todos")
            }
            Button(onClick = { saleOutcomeViewModel.applyFilter(SaleStatus.COMPLETED) }) {
                Text("Completadas")
            }
            Button(onClick = { saleOutcomeViewModel.applyFilter(SaleStatus.CANCELLED) }) {
                Text("Canceladas")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 Lista
        if (sales.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay ventas para este filtro", color = Color.Gray)
            }
        } else {
            LazyColumn(state = salesListState) {
                items(sales) { sale ->
                    SaleOutcomeRow(sale)
                }
            }
        }
    }
}
@Composable
fun SaleOutcomeRow(sale: Sale) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Venta #${sale.id}")
                Text("Items: ${sale.items.size}")
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("Total: ${sale.total}")
                Text(
                    text = sale.status.name,
                    color = when (sale.status) {
                        SaleStatus.COMPLETED -> Color(0xFF2E7D32) // verde
                        SaleStatus.CANCELLED -> Color(0xFFC62828) // rojo
                        else -> Color.Gray
                    }
                )
            }
        }
    }
}