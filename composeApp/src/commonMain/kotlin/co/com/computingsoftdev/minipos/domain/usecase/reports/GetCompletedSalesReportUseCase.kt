package co.com.computingsoftdev.minipos.domain.usecase.reports

import co.com.computingsoftdev.minipos.data.datasource.local.SaleLocalDataSource
import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleStatus

class GetCompletedSalesReportUseCase(
    private val saleLocalDataSource: SaleLocalDataSource
) {
    fun execute(from: Long? = null, to: Long? = null): List<Sale> {
        val completed = saleLocalDataSource.getSalesByStatus(SaleStatus.COMPLETED)
        return completed.filter { sale ->
            val afterFrom = from?.let { sale.date >= it } ?: true
            val beforeTo = to?.let { sale.date <= it } ?: true
            afterFrom && beforeTo
        }
    }
}