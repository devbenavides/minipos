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

    var currentSale by mutableStateOf<Sale?>(null)
        private set

    var pendingSales by mutableStateOf<List<Sale>>(emptyList())
        private set

    var completedSales by mutableStateOf<List<Sale>>(emptyList())
        private set

    var completedFilter by mutableStateOf<SaleStatus?>(null)
        private set

    var cartItems by mutableStateOf<List<SaleItem>>(emptyList())
        private set

    var subtotal by mutableStateOf(0L)
        private set

    var total by mutableStateOf(0L)
        private set

    init {
        loadPendingSales()
        loadCompletedSales()
        startNewSale()
    }

    // Crear una nueva venta y agregarla a pendientes
    fun startNewSale() {
        // 1️⃣ Verificar si ya existe una venta pendiente vacía
        val emptyPending = pendingSales.find { it.items.isEmpty() }
        if (emptyPending != null) {
            currentSale = emptyPending
            return
        }

        // 2️⃣ Crear nueva venta vacía
        val newSale = createSaleUseCase.execute()
        currentSale = newSale

        // 3️⃣ Guardar en DB como pendiente
        saveSaleUseCase.execute(newSale.copy(status = SaleStatus.PENDING))

        // 4️⃣ Recargar pendingSales
        loadPendingSales()
    }

    fun selectPendingSale(sale: Sale) {
        if (sale.items.isEmpty()) {
            // Si es la venta pendiente vacía, solo la cargamos
            currentSale = sale
            recalcTotals()
        } else {
            // Si tiene items, cargamos normalmente
            currentSale = sale
            recalcTotals()
        }
    }

    // Cargar una venta existente (pendiente)
    fun loadSale(saleId: Long) {
        val sale = getSaleByIdUseCase.execute(saleId)
        if (sale != null) {
            currentSale = sale
            recalcTotals()
        }
    }

    fun loadPendingSales() {
        pendingSales = getPendingSalesUseCase.execute()
    }

    fun applyCompletedFilter(status: SaleStatus?) {
        completedFilter = status
        loadCompletedSales() // recarga las ventas aplicando el filtro
    }
    fun loadCompletedSales() {
        //completedSales = getCompletedSalesUseCase.execute()
        completedSales = if (completedFilter == null) {
            getSalesByStatusUseCase.execute(SaleStatus.COMPLETED)
        } else {
            getSalesByStatusUseCase.execute(completedFilter!!)
        }
    }

    fun addItem(item: SaleItem) {
        currentSale?.let { sale ->
            val updatedSale = addItemToSaleUseCase.execute(sale, item)

            currentSale = updatedSale
            recalcTotals()

            // 🔥 IMPORTANTE: persistir cambios
            saveSaleUseCase.execute(updatedSale)
            loadPendingSales()
        }
    }

    fun updateItemQuantity(productId: Long, newQuantity: Int) {
        currentSale?.let { sale ->
            val updated = updateItemQuantityUseCase.execute(sale, productId, newQuantity)
            currentSale = updated
            recalcTotals()

            saveSaleUseCase.execute(updated) // 🔥
            loadPendingSales()
        }
    }

    fun removeItem(productId: Long) {
        currentSale?.let { sale ->

            // Eliminar item de la BD
            removeItemFromSaleUseCase.execute(sale, productId)

            // Actualizar lista en memoria
            val updatedItems = sale.items.filter { it.productId != productId }
            currentSale = sale.copy(items = updatedItems)
            recalcTotals()

            // Guardar la venta solo si no rompe la regla de pendiente vacía única
            saveSaleUseCase.execute(currentSale!!)

            // Limpiar duplicados de ventas vacías
            checkEmptyPendingSales()

            // Recargar pendingSales después de limpieza
            loadPendingSales()
        }
    }

    private fun checkEmptyPendingSales() {
        val emptySales = pendingSales.filter { it.items.isEmpty() }

        // Si hay más de una venta vacía, eliminamos todas menos la primera
        if (emptySales.size > 1) {
            emptySales.drop(1).forEach { duplicate ->
                deleteSaleUseCase.execute(duplicate.id)
            }
        }
    }

    private fun recalcTotals() {
        currentSale?.let { sale ->
            cartItems = sale.items
            subtotal = calculateSubtotalUseCase.execute(sale.items)
            total = calculateTotalUseCase.execute(subtotal)
        }
    }

    fun saveSale() {
        currentSale?.let { sale ->
            val saleToSave = sale.copy(
                subtotal = subtotal,
                total = total,
                status = SaleStatus.COMPLETED
            )

            // Guardar la venta completada
            saveSaleUseCase.execute(saleToSave)

            // 🔹 Recargar pendientes
            val updatedPendings = getPendingSalesUseCase.execute()

            // 🔹 Buscar pendiente vacía o crear nueva
            val emptyPending = updatedPendings.find { it.items.isEmpty() } ?: run {
                val newPending = createSaleUseCase.execute()
                saveSaleUseCase.execute(newPending.copy(status = SaleStatus.PENDING))
                newPending
            }

            // 🔹 Seleccionar automáticamente la pendiente vacía
            currentSale = emptyPending

            // 🔹 Recargar pendientes en la UI
            pendingSales = getPendingSalesUseCase.execute()
            recalcTotals()
            loadCompletedSales()
        }
    }

    fun deletePendingSale(sale: Sale) {
        if (sale.items.isEmpty()) {
            // eliminar de DB
            deleteSaleUseCase.execute(sale.id)
            // recargar ventas pendientes
            loadPendingSales()
            // si era la venta actual, iniciar nueva
            if (currentSale?.id == sale.id) {
                startNewSale()
            }
        }
    }

    fun getSalesLocalDataSource(): SaleLocalDataSource {
        return saleLocalDataSource
    }
}