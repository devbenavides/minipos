package co.com.computingsoftdev.minipos.data.mapper

import co.com.computingsoftdev.minipos.domain.model.Product
import co.com.computingsoftdev.minipos.database.Product as ProductEntity

fun ProductEntity.toDomain(): Product{
    return Product(
        id = id,
        name = name,
        price = price,
        description = description
    )
}

fun Product.toEntity():ProductEntity{
    return ProductEntity(
        id = id,
        name = name,
        price = price,
        description = description
    )
}