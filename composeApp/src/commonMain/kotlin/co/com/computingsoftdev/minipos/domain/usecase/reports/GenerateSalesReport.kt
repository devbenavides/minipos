package co.com.computingsoftdev.minipos.domain.usecase.reports

import co.com.computingsoftdev.minipos.domain.model.ProductReport
import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SalesReport


fun generateReport(sales: List<Sale>): SalesReport {
    val totalVentas = sales.size
    val totalIngresos = sales.sumOf { it.total }
    val ticketPromedio = if (totalVentas > 0) totalIngresos / totalVentas else 0L
    return SalesReport(totalVentas, totalIngresos, ticketPromedio)
}

fun getTopProducts(sales: List<Sale>): List<ProductReport> {
    return sales.flatMap { it.items }
        .groupBy { it.productId }
        .map { (_, items) ->
            val name = items.first().productName
            val quantity = items.sumOf { it.quantity }
            val revenue = items.sumOf { it.subtotal() }
            ProductReport(items.first().productId, name, quantity, revenue)
        }
        .sortedByDescending { it.totalQuantity }
}
