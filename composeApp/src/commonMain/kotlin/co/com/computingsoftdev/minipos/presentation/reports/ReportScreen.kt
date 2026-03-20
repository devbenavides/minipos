package co.com.computingsoftdev.minipos.presentation.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReportScreen(
    reportViewModel: ReportViewModel,
    onBack: () -> Unit
) {
    val report = reportViewModel.salesReport
    val topProducts = reportViewModel.topProducts

    var selectedFilter by remember { mutableStateOf("HOY") }

    LaunchedEffect(Unit) {
        reportViewModel.loadToday()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Reportes", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = onBack) { Text("Volver") }
        }

        Spacer(Modifier.height(12.dp))

        // 🔹 Filtros
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterButton("HOY", selectedFilter) {
                selectedFilter = "HOY"
                reportViewModel.loadToday()
            }
            FilterButton("SEMANA", selectedFilter) {
                selectedFilter = "SEMANA"
                reportViewModel.loadWeek()
            }
            FilterButton("MES", selectedFilter) {
                selectedFilter = "MES"
                reportViewModel.loadMonth()
            }
            FilterButton("TODO", selectedFilter) {
                selectedFilter = "TODO"
                reportViewModel.loadAll()
            }
        }

        Spacer(Modifier.height(16.dp))

        // 🔹 Resumen
        report?.let {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Total Ventas: ${it.totalVentas}")
                    Text("Ingresos: ${it.totalIngresos}")
                    Text("Ticket Promedio: ${it.ticketPromedio}")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("Top Productos", style = MaterialTheme.typography.titleMedium)

        LazyColumn {
            items(topProducts) { p ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(p.productName)
                    Text("Cant: ${p.totalQuantity}")
                    Text("$${p.totalRevenue}")
                }
            }
        }
    }
}