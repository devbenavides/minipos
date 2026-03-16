package co.com.computingsoftdev.minipos.domain.repository

import co.com.computingsoftdev.minipos.domain.model.Product

interface ProductRepository {
    fun getProducts(): List<Product>

    fun addProduct(product: Product)

    fun updateProduct(product: Product)

    fun deleteProduct(id: Long)
}