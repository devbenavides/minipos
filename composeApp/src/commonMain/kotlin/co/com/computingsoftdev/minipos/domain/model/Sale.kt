package co.com.computingsoftdev.minipos.domain.model

data class Sale(
    val id: Long,                   // ID de la venta (autoincrement en DB)
    val date: Long,                 // Timestamp de la venta
    val subtotal: Long,             // Suma de los subtotales de los items
    val total: Long,                // Total final (subtotal + impuestos/descuentos)
    val status: SaleStatus,         // Estado: PENDING, COMPLETED, CANCELLED
    val items: List<SaleItem> = emptyList() // Lista de items de la venta
) {
    // Recalcula subtotal sumando los items
    fun recalcSubtotal(): Long = items.sumOf { it.subtotal() }

    // Recalcula total (puedes agregar impuestos o descuentos aquí)
    fun recalcTotal(tax: Long = 0L, discount: Long = 0L): Long =
        recalcSubtotal() + tax - discount
}
