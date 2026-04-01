package co.com.computingsoftdev.minipos.presentation.sales

import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleItem
import co.com.computingsoftdev.minipos.domain.model.SaleStatus

data class SaleUiState(
    var currentSale: Sale? = null,
    val pendingSales: List<Sale> = emptyList(),
    val completedSales: List<Sale> = emptyList(),
    val subtotal: Long = 0L,
    val total: Long = 0L,
    val completedFilter: SaleStatus? = null
)
