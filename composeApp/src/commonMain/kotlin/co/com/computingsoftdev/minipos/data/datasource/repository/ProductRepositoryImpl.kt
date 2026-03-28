package co.com.computingsoftdev.minipos.data.datasource.repository

import co.com.computingsoftdev.minipos.data.datasource.local.ProductLocalDataSource
import co.com.computingsoftdev.minipos.data.mapper.toDomain
import co.com.computingsoftdev.minipos.domain.model.Product
import co.com.computingsoftdev.minipos.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val localDataSource: ProductLocalDataSource
) : ProductRepository {
    override fun getProducts(): List<Product> {
        return localDataSource
            .getAll().map { it.toDomain() }
    }

    override fun addProduct(product: Product) {
        localDataSource.insert(
            name = product.name,
            price = product.price,
            description = product.description
        )
    }

    override fun updateProduct(product: Product) {
        localDataSource.update(
            id = product.id,
            name = product.name,
            price = product.price,
            description = product.description
        )
    }

    override fun deleteProduct(id: Long) {
        localDataSource.delete(id)
    }
}