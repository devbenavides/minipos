package co.com.computingsoftdev.minipos.presentation.components

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleStatus

@Composable
fun SaleListComponent(
    sales: List<Sale>,
    onSelect: (Long) -> Unit
) {
    LazyRow {
        items(sales.filter { it.status == SaleStatus.PENDING }) { sale ->
            Button(onClick = { onSelect(sale.id) }) {
                Text("Venta ${sale.id}")
            }
        }
    }
}