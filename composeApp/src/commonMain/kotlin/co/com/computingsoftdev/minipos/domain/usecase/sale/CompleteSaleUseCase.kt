package co.com.computingsoftdev.minipos.domain.usecase.sale

import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleStatus
import co.com.computingsoftdev.minipos.domain.repository.SaleRepository

class CompleteSaleUseCase(
    private val repository: SaleRepository
) {

    fun execute(sale: Sale) {
        val completed = sale.copy(status = SaleStatus.COMPLETED)
        repository.saveSale(completed)
    }
}