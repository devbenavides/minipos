package co.com.computingsoftdev.minipos.domain.repository

import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleItem

interface SaleRepository {

    /** Guarda o actualiza una venta completa con sus items */
    fun saveSale(sale: Sale): Long

    /** Obtiene todas las ventas */
    fun getAllSales(): List<Sale>

    /** Obtiene todas las ventas pendientes (PENDING) */
    fun getPendingSales(): List<Sale>

    /** Obtiene items de una venta específica */
    fun getItemsBySale(saleId: Long): List<SaleItem>

    /** Opcional: obtener una venta completa por ID */
    fun getSaleById(saleId: Long): Sale?
    fun deleteSale(saleId: Long)
    fun deleteSaleItem(saleId: Long, productId: Long)
}