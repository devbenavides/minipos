package co.com.computingsoftdev.minipos.presentation.sales.sale_outcome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleStatus
import co.com.computingsoftdev.minipos.domain.usecase.sale.GetSalesByStatusUseCase

class SaleOutcomeViewModel(
    private val getSalesByStatusUseCase: GetSalesByStatusUseCase
) : ViewModel() {
    var completedSales by mutableStateOf<List<Sale>>(emptyList())
        private set

    var completedFilter by mutableStateOf<SaleStatus?>(null)
        private set

    init {
        loadSales()
    }

    fun applyFilter(status: SaleStatus?) {
        completedFilter = status
        loadSales() // recarga las ventas aplicando el filtro
    }

    fun loadSales() {
        //completedSales = getCompletedSalesUseCase.execute()
        completedSales = if (completedFilter == null) {
            getSalesByStatusUseCase.execute(SaleStatus.COMPLETED)
        } else {
            getSalesByStatusUseCase.execute(completedFilter!!)
        }
    }

    fun refresh() {
        loadSales()
    }

}