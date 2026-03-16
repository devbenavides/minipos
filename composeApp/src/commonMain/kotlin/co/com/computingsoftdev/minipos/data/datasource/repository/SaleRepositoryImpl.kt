package co.com.computingsoftdev.minipos.data.datasource.repository

import co.com.computingsoftdev.minipos.data.datasource.local.SaleLocalDataSource
import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleItem
import co.com.computingsoftdev.minipos.domain.repository.SaleRepository

class SaleRepositoryImpl (
    private val local: SaleLocalDataSource
): SaleRepository{
    // Guardar una venta completa
    override fun saveSale(sale: Sale) {
        // Insertar la venta en la base de datos
        local.insertSale(sale)
    }

    // Obtener todas las ventas
    override fun getAllSales(): List<Sale> {
        return local.getAllSales()
    }

    // Obtener items de una venta específica
    override fun getItemsBySale(saleId: Long): List<SaleItem> {
        return local.getItemsBySale(saleId)
    }
}