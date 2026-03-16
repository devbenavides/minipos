package co.com.computingsoftdev.minipos.data.datasource.repository

import co.com.computingsoftdev.minipos.data.datasource.local.ProductLocalDataSource
import co.com.computingsoftdev.minipos.domain.model.Product
import co.com.computingsoftdev.minipos.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val local: ProductLocalDataSource
): ProductRepository {
    override fun getProducts(): List<Product> {
        return local.getAll().map {

            Product(
                id = it.id,
                name = it.name,
                price = it.price,
                description = it.description
            )

        }
    }

    override fun addProduct(product: Product) {
        local.insert(
            name = product.name,
            price = product.price,
            description = product.description
        )
    }

    override fun updateProduct(product: Product) {
        local.update(
            id = product.id,
            name = product.name,
            price = product.price,
            description = product.description
        )
    }

    override fun deleteProduct(id: Long) {
        local.delete(id)
    }
}