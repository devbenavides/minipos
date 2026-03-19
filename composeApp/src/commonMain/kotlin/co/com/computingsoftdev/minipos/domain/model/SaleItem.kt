package co.com.computingsoftdev.minipos.domain.model

data class SaleItem(
    val id: Long,           // ID de la tabla SaleItem (autoincrement)
    val saleId: Long,       // ID de la venta a la que pertenece
    val productId: Long,    // ID del producto
    val productName: String,// Nombre del producto en el momento de la venta
    val price: Long,        // Precio unitario congelado al momento de la venta
    val quantity: Int       // Cantidad vendida
) {
    fun subtotal(): Long = price * quantity
}
