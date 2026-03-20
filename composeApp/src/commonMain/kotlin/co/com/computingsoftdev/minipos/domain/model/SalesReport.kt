package co.com.computingsoftdev.minipos.domain.model

data class SalesReport(
    val totalVentas: Int,
    val totalIngresos: Long,
    val ticketPromedio: Long
)