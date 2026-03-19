package co.com.computingsoftdev.minipos.domain.usecase.sale

import co.com.computingsoftdev.minipos.domain.model.Sale

class UpdateItemQuantityUseCase {
    fun execute(sale: Sale, productId: Long, newQuantity: Int): Sale {
        val items = sale.items.map {
            if (it.productId == productId) it.copy(quantity = newQuantity) else it
        }
        return sale.copy(items = items)
    }
}