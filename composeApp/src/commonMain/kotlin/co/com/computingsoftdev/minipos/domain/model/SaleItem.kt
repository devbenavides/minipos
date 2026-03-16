package co.com.computingsoftdev.minipos.domain.model

data class SaleItem(
    val product: Product,
    val quantity: Int
) {
    fun subtotal(): Long = product.price * quantity
}
