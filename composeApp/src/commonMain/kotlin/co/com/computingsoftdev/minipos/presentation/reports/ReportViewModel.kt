package co.com.computingsoftdev.minipos.presentation.reports

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import co.com.computingsoftdev.minipos.core.utils.DateUtils
import co.com.computingsoftdev.minipos.domain.model.ProductReport
import co.com.computingsoftdev.minipos.domain.model.SalesReport
import co.com.computingsoftdev.minipos.domain.usecase.reports.GetCompletedSalesReportUseCase
import co.com.computingsoftdev.minipos.domain.usecase.reports.generateReport
import co.com.computingsoftdev.minipos.domain.usecase.reports.getTopProducts


class ReportViewModel(
    private val getCompletedSalesReportUseCase: GetCompletedSalesReportUseCase
) : ViewModel() {

    var salesReport by mutableStateOf<SalesReport?>(null)
        private set

    var topProducts by mutableStateOf<List<ProductReport>>(emptyList())
        private set

    fun loadReport(from: Long? = null, to: Long? = null) {
        val sales = getCompletedSalesReportUseCase.execute(from, to)
        salesReport = generateReport(sales)
        topProducts = getTopProducts(sales)
    }

    fun loadToday() {
        loadReport(DateUtils.startOfToday(), DateUtils.now())
    }

    fun loadWeek() {
        loadReport(DateUtils.startOfWeek(), DateUtils.now())
    }

    fun loadMonth() {
        loadReport(DateUtils.startOfMonth(), DateUtils.now())
    }

    fun loadAll() {
        loadReport(null, null)
    }
}