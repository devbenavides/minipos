package co.com.computingsoftdev.minipos.domain.usecase.sale

import co.com.computingsoftdev.minipos.domain.repository.SaleRepository

class DeleteSaleUseCase(
    private val saleRepository: SaleRepository
) {
    fun execute(saleId: Long) {
        saleRepository.deleteSale(saleId)
    }
}