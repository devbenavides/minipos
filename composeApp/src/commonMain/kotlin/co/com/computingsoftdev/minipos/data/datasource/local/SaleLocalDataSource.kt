package co.com.computingsoftdev.minipos.data.datasource.local

import co.com.computingsoftdev.minipos.database.SaleItemQueries
import co.com.computingsoftdev.minipos.database.SaleQueries
import co.com.computingsoftdev.minipos.domain.model.Product
import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleItem

class SaleLocalDataSource(
    private val saleQueries: SaleQueries,
    private val saleItemQueries: SaleItemQueries,
    private val productLocalDataSource: ProductLocalDataSource
) {
    fun insertSale(
        sale: Sale
    ) {
        saleQueries.insertSale(
            date = sale.date,
            total = sale.total
        )

        val saleId = saleQueries.selectAllSales().executeAsList().last().id

        // Insertar los items de la venta
        sale.items.forEach { item ->
            saleItemQueries.insertSaleItem(
                saleId = saleId,
                productId = item.product.id,
                quantity = item.quantity.toLong(),
                subtotal = item.subtotal()
            )
        }
    }

    // Obtener todos los items de una venta
    fun getItemsBySale(saleId: Long): List<SaleItem> {
        val entities = saleItemQueries.selectItemsBySale(saleId).executeAsList()
        val items = mutableListOf<SaleItem>()

        for (entity in entities) {
            val product = productLocalDataSource.getById(entity.productId) ?: continue
            items.add(
                SaleItem(
                    product = product,
                    quantity = entity.quantity.toInt()
                )
            )
        }

        return items
    }

    // Opcional: obtener todas las ventas
    fun getAllSales(): List<Sale> {
        return saleQueries.selectAllSales().executeAsList().map { entity ->
            val items = getItemsBySale(entity.id)
            val subtotal = items.sumOf { it.subtotal() } // ✅ calcular subtotal real

            Sale(
                id = entity.id,
                items = items,
                subtotal = subtotal,
                total = entity.total.toLong(), // si tu DB usa Double, convertir a Long
                date = entity.date
            )
        }
    }
}