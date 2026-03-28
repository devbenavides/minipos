package co.com.computingsoftdev.minipos.data.mapper

import co.com.computingsoftdev.minipos.database.SelectSaleWithItems
import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleItem
import co.com.computingsoftdev.minipos.domain.model.SaleStatus
import co.com.computingsoftdev.minipos.database.Sale as SaleEntity

fun SaleEntity.toDomain(items: List<SaleItem> = emptyList()): Sale{
    return Sale(
        id = id,
        date = date,
        subtotal = subtotal,
        total = total,
        status = SaleStatus.valueOf(status),
        items = items
    )
}

fun Sale.toEntity(): SaleEntity {
    return SaleEntity(
        id = id,
        date = date,
        subtotal = subtotal,
        total = total,
        status = status.name
    )
}