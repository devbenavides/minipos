package co.com.computingsoftdev.minipos.data.datasource.local

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

    /**
     * Inserta una venta completa con sus items.
     * Calcula subtotal en memoria antes de guardar.
     */
    fun saveSale(sale: Sale):Long {
        return if (sale.id == 0L) {
            // 🔹 INSERT
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
            // 🔹 UPDATE
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
                price = item.price
            )
        }
    }


    /**
     * Obtiene todos los items de una venta
     */
    fun getItemsBySale(saleId: Long): List<SaleItem> {
        val entities = saleItemQueries.selectItemsBySale(saleId).executeAsList()

        return entities.map { entity ->
            val productName = productLocalDataSource.getById(entity.productId)?.name ?: "Producto"
            SaleItem(
                id = entity.id,
                saleId = saleId,
                productId = entity.productId,
                productName = productName,
                price = entity.price,
                quantity = entity.quantity.toInt()
            )
        }
    }

    /**
     * Obtener todas las ventas con sus items
     */
    fun getAllSales(): List<Sale> {
        return saleQueries.selectAllSales().executeAsList().map { entity ->
            val items = getItemsBySale(entity.id)
            val subtotal = items.sumOf { it.subtotal() }

            Sale(
                id = entity.id,
                items = items,
                subtotal = subtotal,
                total = entity.total.toLong(),
                date = entity.date,
                status = when (entity.status) {
                    "PENDING" -> SaleStatus.PENDING
                    "COMPLETED" -> SaleStatus.COMPLETED
                    "CANCELLED" -> SaleStatus.CANCELLED
                    else -> SaleStatus.PENDING
                }
            )
        }
    }

    /**
     * Opcional: obtener ventas pendientes
     */
    fun getPendingSales(): List<Sale> {
        val saleEntities = saleQueries.selectAllSales().executeAsList()
            .filter { it.status == "PENDING" }

        return saleEntities.map { saleEntity ->
            val items = saleItemQueries.selectItemsBySale(saleEntity.id)
                .executeAsList()
                .mapNotNull { itemEntity ->
                    val product = productLocalDataSource.getById(itemEntity.productId) ?: return@mapNotNull null
                    SaleItem(
                        id = itemEntity.id,
                        saleId = saleEntity.id,
                        productId = product.id,
                        productName = product.name,
                        price = product.price,
                        quantity = itemEntity.quantity.toInt()
                    )
                }

            Sale(
                id = saleEntity.id,
                items = items,
                subtotal = items.sumOf { it.subtotal() },
                total = items.sumOf { it.subtotal() }, // o aplicar impuestos si los tienes
                date = saleEntity.date,
                status = SaleStatus.PENDING
            )
        }
    }

    fun deleteSale(saleId: Long) {
        // Primero borramos los items de la venta
        saleItemQueries.deleteItemsBySale(saleId)
        // Luego borramos la venta
        saleQueries.deleteSale(saleId)
    }

    fun deleteSaleItem(saleId: Long, productId: Long) {
        saleItemQueries.deleteSaleItemBySaleAndProduct(saleId, productId)
    }
}