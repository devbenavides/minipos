package co.com.computingsoftdev.minipos.domain.usecase.sale

import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.repository.SaleRepository

class SaveSaleUseCase(
    private val repository: SaleRepository
) {

    fun execute(sale: Sale) {
        repository.saveSale(sale)
    }
}