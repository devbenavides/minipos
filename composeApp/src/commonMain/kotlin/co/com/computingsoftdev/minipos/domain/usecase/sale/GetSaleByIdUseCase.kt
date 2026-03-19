package co.com.computingsoftdev.minipos.domain.usecase.sale

import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.repository.SaleRepository

class GetSaleByIdUseCase(
    private val repository: SaleRepository
) {
    fun execute(saleId: Long): Sale? = repository.getSaleById(saleId)
}