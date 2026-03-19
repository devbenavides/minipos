package co.com.computingsoftdev.minipos.domain.usecase.sale

import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.repository.SaleRepository

class GetPendingSalesUseCase(
    private val repository: SaleRepository
) {
    fun execute(): List<Sale> = repository.getPendingSales()
}