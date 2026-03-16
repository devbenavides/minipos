package co.com.computingsoftdev.minipos.presentation.sales

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleItem
import co.com.computingsoftdev.minipos.domain.usecase.sale.*
import kotlin.time.ExperimentalTime

class SaleViewModel(
    private val addItemToSaleUseCase: AddItemToSaleUseCase,
    private val calculateSubtotalUseCase: CalculateSubtotalUseCase,
    private val calculateTotalUseCase: CalculateTotalUseCase,
    private val saveSaleUseCase: SaveSaleUseCase
) : ViewModel() {
    var cartItems by mutableStateOf<List<SaleItem>>(emptyList())
        private set

    var subtotal by mutableStateOf(0L)
        private set

    var total by mutableStateOf(0L)
        private set

    // Agrega un item al carrito
    fun addItem(item: SaleItem) {
        val mutableCart = cartItems.toMutableList()
        addItemToSaleUseCase.execute(mutableCart, item)
        cartItems = mutableCart
        recalcTotals()
    }

    // Recalcula subtotal y total
    private fun recalcTotals() {
        subtotal = calculateSubtotalUseCase.execute(cartItems)
        total = calculateTotalUseCase.execute(subtotal)
    }

    // Guarda la venta
    fun saveSale(date: Long) {
        val sale = Sale(
            id = 0L, // SQLite generará el id
            items = cartItems,
            subtotal = subtotal,
            total = total,
            date = date
        )
        saveSaleUseCase.execute(sale)
        // Limpiar carrito
        cartItems = emptyList()
        subtotal = 0L
        total = 0L
    }
}