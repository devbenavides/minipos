package co.com.computingsoftdev.minipos.data.datasource.local

import co.com.computingsoftdev.minipos.core.extensions.toSaleStatus
import co.com.computingsoftdev.minipos.database.SaleItemQueries
import co.com.computingsoftdev.minipos.database.SaleQueries
import co.com.computingsoftdev.minipos.domain.model.Product
import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleItem
import co.com.computingsoftdev.minipos.domain.model.SaleStatus

class SaleLocalDataSource(
    private val saleQueries: SaleQueries,
    private val saleItemQueries: SaleItemQueries,
    private val productLocalDataSource: ProductLocalDataSource
) {

    fun saveSale(sale: Sale): Long {
        return if (sale.id == 0L) {

            saleQueries.insertSale(
                date = sale.date,
                subtotal = sale.subtotal,
                total = sale.total,
                status = sale.status.name
            )

            val saleId = saleQueries.selectLastInsertedId().executeAsOne()

            insertItems(saleId, sale.items)
            saleId

        } else {

            saleQueries.updateSale(
                date = sale.date,
                subtotal = sale.subtotal,
                total = sale.total,
                status = sale.status.name,
                id = sale.id
            )

            saleItemQueries.deleteItemsBySale(sale.id)
            insertItems(sale.id, sale.items)

            sale.id
        }
    }

    private fun insertItems(saleId: Long, items: List<SaleItem>) {
        items.forEach { item ->
            saleItemQueries.insertSaleItem(
                saleId = saleId,
                productId = item.productId,
                quantity = item.quantity.toLong(),
                price = item.price // 🔥 correcto
            )
        }
    }

    /**
     * Obtener items de una venta
     */
    fun getItemsBySale(saleId: Long): List<SaleItem> {
        return saleItemQueries
            .selectItemsBySale(saleId)
            .executeAsList()
            .mapNotNull { entity ->

                val product = productLocalDataSource.getById(entity.productId)
                    ?: return@mapNotNull null

                SaleItem(
                    id = entity.id,
                    saleId = saleId,
                    productId = entity.productId,
                    productName = product.name,
                    price = entity.price, // 🔥 FIX importante
                    quantity = entity.quantity.toInt()
                )
            }
    }

    /**
     * 🔥 Construye Sale desde resultado de SQLDelight
     * (sin depender de SaleEntity)
     */
    private fun buildSale(
        id: Long,
        date: Long,
        total: Long,
        status: String
    ): Sale {

        val items = getItemsBySale(id)

        return Sale(
            id = id,
            items = items,
            subtotal = items.sumOf { it.subtotal() },
            total = total,
            date = date,
            status = status.toSaleStatus()
        )
    }

    /**
     * Obtener todas las ventas
     */
    fun getAllSales(): List<Sale> {
        return saleQueries
            .selectAllSales()
            .executeAsList()
            .map {
                buildSale(
                    id = it.id,
                    date = it.date,
                    total = it.total,
                    status = it.status
                )
            }
    }

    /**
     * 🔥 BASE: obtener ventas por estado
     */
    fun getSalesByStatus(status: SaleStatus): List<Sale> {

        val sales = saleQueries
            .selectAllSales()
            .executeAsList()
            .filter { it.status == status.name }
        println("DEBUG: ventas con status ${status.name} = ${sales.size}")
        return sales.map {
            buildSale(
                id = it.id,
                date = it.date,
                total = it.total,
                status = it.status
            )
        }
    }

    /**
     * Ventas pendientes (sin duplicación)
     */
    fun getPendingSales(): List<Sale> {
        return getSalesByStatus(SaleStatus.PENDING)
    }

    fun getCompletedSales(): List<Sale> {
        return getSalesByStatus(SaleStatus.COMPLETED)
    }

    fun deleteSale(saleId: Long) {
        saleItemQueries.deleteItemsBySale(saleId)
        saleQueries.deleteSale(saleId)
    }

    fun deleteSaleItem(saleId: Long, productId: Long) {
        saleItemQueries.deleteSaleItemBySaleAndProduct(saleId, productId)
    }
}