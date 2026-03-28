package co.com.computingsoftdev.minipos.data.mapper

import co.com.computingsoftdev.minipos.domain.model.SaleItem
import co.com.computingsoftdev.minipos.database.SaleItem as SaleItemEntity


fun SaleItemEntity.toDomain(): SaleItem {
    return SaleItem(
        id = id,
        saleId = saleId,
        productId = productId,
        productName = productName,
        price = price,
        quantity = quantity.toInt()
    )
}

fun SaleItem.toEntity(): SaleItemEntity {
    return SaleItemEntity(
        id = id,
        saleId = saleId,
        productId = productId,
        productName = productName,
        price = price,
        quantity = quantity.toLong()
    )
}