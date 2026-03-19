package co.com.computingsoftdev.minipos.data.datasource.repository

import co.com.computingsoftdev.minipos.data.datasource.local.SaleLocalDataSource
import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleItem
import co.com.computingsoftdev.minipos.domain.repository.SaleRepository

class SaleRepositoryImpl(
    private val local: SaleLocalDataSource
) : SaleRepository {

    // Guarda o actualiza una venta completa
    override fun saveSale(sale: Sale): Long {
        // Aquí puedes diferenciar si es nueva venta o actualización
        // Por simplicidad, siempre insertamos
        return local.saveSale(sale)
    }

    // Obtener todas las ventas
    override fun getAllSales(): List<Sale> {
        return local.getAllSales()
    }

    // Obtener ventas pendientes
    override fun getPendingSales(): List<Sale> {
        return local.getPendingSales()
    }

    // Obtener items de una venta específica
    override fun getItemsBySale(saleId: Long): List<SaleItem> {
        return local.getItemsBySale(saleId)
    }

    // Obtener una venta completa por ID
    override fun getSaleById(saleId: Long): Sale? {
        // Buscar en todas las ventas
        return local.getAllSales().find { it.id == saleId }
    }

    override fun deleteSale(saleId: Long) {
        local.deleteSale(saleId)
    }

    override fun deleteSaleItem(saleId: Long, productId: Long) {
        local.deleteSaleItem(saleId,productId)
    }
}