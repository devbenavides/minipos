package co.com.computingsoftdev.minipos.domain.usecase.sale

import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleStatus
import co.com.computingsoftdev.minipos.domain.repository.SaleRepository

class GetSalesByStatusUseCase(
    private val repository: SaleRepository
) {
    fun execute(status: SaleStatus): List<Sale> {
        return repository.getSalesByStatus(status)
    }
}