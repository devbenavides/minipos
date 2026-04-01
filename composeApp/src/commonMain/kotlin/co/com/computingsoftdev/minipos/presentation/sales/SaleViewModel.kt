package co.com.computingsoftdev.minipos.presentation.sales

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import co.com.computingsoftdev.minipos.data.datasource.local.SaleLocalDataSource
import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleItem
import co.com.computingsoftdev.minipos.domain.model.SaleStatus
import co.com.computingsoftdev.minipos.domain.usecase.sale.*

class SaleViewModel(
    private val createSaleUseCase: CreateSaleUseCase,
    private val addItemToSaleUseCase: AddItemToSaleUseCase,
    private val updateItemQuantityUseCase: UpdateItemQuantityUseCase,
    private val removeItemFromSaleUseCase: RemoveItemFromSaleUseCase,
    private val calculateSubtotalUseCase: CalculateSubtotalUseCase,
    private val calculateTotalUseCase: CalculateTotalUseCase,
    private val saveSaleUseCase: SaveSaleUseCase,
    private val getPendingSalesUseCase: GetPendingSalesUseCase,
    private val getCompletedSalesUseCase: GetCompletedSalesUseCase,
    private val getSaleByIdUseCase: GetSaleByIdUseCase,
    private val deleteSaleUseCase: DeleteSaleUseCase,
    private val saleLocalDataSource: SaleLocalDataSource,
    private val getSalesByStatusUseCase: GetSalesByStatusUseCase
) : ViewModel() {

    var uiState by mutableStateOf(SaleUiState())
        private set

    init {
        loadPendingSales()
        loadCompletedSales()
        startNewSale()
    }

    // Crear una nueva venta y agregarla a pendientes
    fun startNewSale() {
        // 🔹 1. Asegurar que tenemos datos actualizados
        val pending = getPendingSalesUseCase.execute()

        // 🔹 2. Buscar venta vacía existente
        val emptyPending = pending.find { it.items.isEmpty() }

        if (emptyPending != null) {
            uiState = uiState.copy(
                currentSale = emptyPending,
                pendingSales = pending
            )
            recalcTotals()
            return
        }

        // 🔹 3. Crear nueva venta
        val newSale = createSaleUseCase.execute()

        // 🔹 4. Guardar como PENDING
        val pendingSale = newSale.copy(status = SaleStatus.PENDING)
        saveSaleUseCase.execute(pendingSale)

        // 🔹 5. Recargar lista ya persistida
        val updatedPending = getPendingSalesUseCase.execute()

        uiState = uiState.copy(
            currentSale = pendingSale,
            pendingSales = updatedPending
        )

        recalcTotals()
    }

    fun selectPendingSale(sale: Sale) {
        uiState = uiState.copy(currentSale = sale)
        recalcTotals()
    }

    // Cargar una venta existente (pendiente)
    fun loadSale(saleId: Long) {
        val sale = getSaleByIdUseCase.execute(saleId) ?: return
        uiState = uiState.copy(currentSale = sale)
        recalcTotals()
    }

    fun loadPendingSales() {
        val sales = getPendingSalesUseCase.execute()
        uiState = uiState.copy(
            pendingSales = sales
        )
    }

    fun applyCompletedFilter(status: SaleStatus?) {
        uiState = uiState.copy(completedFilter = status)
        loadCompletedSales()
    }

    fun loadCompletedSales() {
        val sales = if (uiState.completedFilter == null) {
            getSalesByStatusUseCase.execute(SaleStatus.COMPLETED)
        } else {
            getSalesByStatusUseCase.execute(uiState.completedFilter!!)
        }

        uiState = uiState.copy(completedSales = sales)
    }

    fun addItem(item: SaleItem) {
        val sale = uiState.currentSale ?: return

        val updatedSale = addItemToSaleUseCase.execute(sale, item)

        saveSaleUseCase.execute(updatedSale)

        loadPendingSales()

        uiState = uiState.copy(currentSale = updatedSale)

        recalcTotals()
    }

    fun updateItemQuantity(productId: Long, newQuantity: Int) {
        val sale = uiState.currentSale ?: return

        val updated = updateItemQuantityUseCase.execute(sale, productId, newQuantity)

        saveSaleUseCase.execute(updated)

        loadPendingSales()

        uiState = uiState.copy(currentSale = updated)

        recalcTotals()
    }

    fun removeItem(productId: Long) {
        val sale = uiState.currentSale ?: return

        removeItemFromSaleUseCase.execute(sale, productId)

        val updatedItems = sale.items.filter { it.productId != productId }
        val updatedSale = sale.copy(items = updatedItems)

        saveSaleUseCase.execute(updatedSale)

        checkEmptyPendingSales()
        loadPendingSales()

        uiState = uiState.copy(currentSale = updatedSale)

        recalcTotals()
    }

    private fun checkEmptyPendingSales() {
        val emptySales = uiState.pendingSales.filter { it.items.isEmpty() }

        if (emptySales.size > 1) {
            emptySales.drop(1).forEach { duplicate ->
                deleteSaleUseCase.execute(duplicate.id)
            }
        }
    }

    private fun recalcTotals() {
        val sale = uiState.currentSale ?: return
        val subtotal = calculateSubtotalUseCase.execute(sale.items)
        val total = calculateTotalUseCase.execute(subtotal)

        uiState = uiState.copy(
            subtotal = subtotal,
            total = total
        )

    }

    fun saveSale() {
        val sale = uiState.currentSale ?: return
        val saleToSave = sale.copy(
            subtotal = uiState.subtotal,
            total = uiState.total,
            status = SaleStatus.COMPLETED
        )
        // Guardar la venta completada
        saveSaleUseCase.execute(saleToSave)

        val pending = getPendingSalesUseCase.execute()
        // 🔹 Buscar pendiente vacía o crear nueva
        val emptyPending = pending.find { it.items.isEmpty() } ?: run {
            val newSale = createSaleUseCase.execute()
            saveSaleUseCase.execute(newSale.copy(status = SaleStatus.PENDING))
            newSale
        }

        uiState = uiState.copy(
            currentSale = emptyPending,
            pendingSales = pending
        )

        recalcTotals()
        loadCompletedSales()

    }

    fun deletePendingSale(sale: Sale) {
        if (sale.items.isEmpty()) {
            deleteSaleUseCase.execute(sale.id)

            loadPendingSales()

            if (uiState.currentSale?.id == sale.id) {
                startNewSale()
            }
        }
    }

    fun cancelSale(sale: Sale) {
        if (sale.items.isEmpty()) {
            deleteSaleUseCase.execute(sale.id)
        } else {
            val cancelled = sale.copy(status = SaleStatus.CANCELLED)
            saveSaleUseCase.execute(cancelled)
        }

        loadPendingSales()

        if (uiState.currentSale?.id == sale.id) {
            startNewSale()
        }
    }

    fun getSalesLocalDataSource(): SaleLocalDataSource {
        return saleLocalDataSource
    }
}