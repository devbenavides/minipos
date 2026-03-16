package co.com.computingsoftdev.minipos.domain.usecase.sale

import co.com.computingsoftdev.minipos.domain.model.SaleItem

class CalculateSubtotalUseCase {
    fun execute(items: List<SaleItem>): Long {
        return items.sumOf { it.subtotal() }
    }
}