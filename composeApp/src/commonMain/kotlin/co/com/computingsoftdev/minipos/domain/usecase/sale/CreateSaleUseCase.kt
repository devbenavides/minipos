package co.com.computingsoftdev.minipos.domain.usecase.sale

import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleStatus
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class CreateSaleUseCase {
    @OptIn(ExperimentalTime::class)
    fun execute(): Sale {
        val now = Clock.System.now().toEpochMilliseconds()
        return Sale(
            id = 0L, // DB asignará autoincrement
            date = now,
            subtotal = 0L,
            total = 0L,
            status = SaleStatus.PENDING,
            items = emptyList()
        )
    }
}