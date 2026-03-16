package co.com.computingsoftdev.minipos.domain.usecase.sale

import co.com.computingsoftdev.minipos.domain.model.SaleItem

class AddItemToSaleUseCase {
    fun execute(cartItems: MutableList<SaleItem>, itemToAdd: SaleItem) {
        val existingItem = cartItems.find { it.product.id == itemToAdd.product.id }

        if (existingItem != null) {
            val index = cartItems.indexOf(existingItem)
            cartItems[index] = existingItem.copy(
                quantity = existingItem.quantity + itemToAdd.quantity
            )
        } else {
            cartItems.add(itemToAdd)
        }
    }
}