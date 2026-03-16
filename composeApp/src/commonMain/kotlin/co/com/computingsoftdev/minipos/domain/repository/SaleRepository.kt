package co.com.computingsoftdev.minipos.domain.repository

import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleItem

interface SaleRepository {
    fun saveSale(sale: Sale)
    fun getAllSales(): List<Sale>
    fun getItemsBySale(saleId: Long): List<SaleItem>
}