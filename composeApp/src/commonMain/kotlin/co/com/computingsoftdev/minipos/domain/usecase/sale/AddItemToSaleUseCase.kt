package co.com.computingsoftdev.minipos.domain.usecase.sale

import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleItem

class AddItemToSaleUseCase {
    fun execute(sale: Sale, item: SaleItem): Sale {
        val items = sale.items.toMutableList()

        // Verificar si el item ya existe
        val existing = items.find { it.productId == item.productId }
        if (existing != null) {
            val index = items.indexOf(existing)
            items[index] = existing.copy(quantity = existing.quantity + item.quantity)
        } else {
            items.add(item)
        }

        return sale.copy(items = items)
    }
}