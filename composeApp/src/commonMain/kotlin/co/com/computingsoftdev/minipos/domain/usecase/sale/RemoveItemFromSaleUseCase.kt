package co.com.computingsoftdev.minipos.domain.usecase.sale

import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.repository.SaleRepository

class RemoveItemFromSaleUseCase(
    private val repository: SaleRepository
) {
    fun execute(sale: Sale, productId: Long): Sale {
        // Borrar item de la BD
        repository.deleteSaleItem(sale.id, productId)

        // Filtrar en memoria
        val updatedItems = sale.items.filter { it.productId != productId }

        return sale.copy(items = updatedItems)
    }
}