package co.com.computingsoftdev.minipos.domain.service

import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleItem

class SaleManager {

    /**
     * Agrega un item a la venta.
     * Si el producto ya existe, suma la cantidad.
     * Recalcula subtotal y total automáticamente.
     */
    fun addItem(sale: Sale, item: SaleItem): Sale {
        val updatedItems = sale.items.toMutableList().apply {
            val index = indexOfFirst { it.productId == item.productId }

            if (index >= 0) {
                // Producto ya existe, sumar cantidades
                val existing = get(index)
                set(index, existing.copy(quantity = existing.quantity + item.quantity))
            } else {
                // Nuevo producto
                add(item)
            }
        }

        // Recalcular subtotal y total
        val subtotal = updatedItems.sumOf { it.subtotal() }
        val total = subtotal // si quieres agregar impuestos, descuentos, ajusta aquí

        return sale.copy(
            items = updatedItems,
            subtotal = subtotal,
            total = total
        )
    }

    /** Actualiza la cantidad de un producto */
    fun updateItemQuantity(sale: Sale, productId: Long, newQuantity: Int): Sale {
        val updatedItems = sale.items.map {
            if (it.productId == productId) it.copy(quantity = newQuantity) else it
        }
        val subtotal = updatedItems.sumOf { it.subtotal() }
        val total = subtotal
        return sale.copy(items = updatedItems, subtotal = subtotal, total = total)
    }

    /** Elimina un producto de la venta */
    fun removeItem(sale: Sale, productId: Long): Sale {
        val updatedItems = sale.items.filter { it.productId != productId }
        val subtotal = updatedItems.sumOf { it.subtotal() }
        val total = subtotal
        return sale.copy(items = updatedItems, subtotal = subtotal, total = total)
    }
}