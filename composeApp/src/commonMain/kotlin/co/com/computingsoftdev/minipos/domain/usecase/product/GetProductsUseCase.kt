package co.com.computingsoftdev.minipos.domain.usecase.product

import co.com.computingsoftdev.minipos.domain.model.Product
import co.com.computingsoftdev.minipos.domain.repository.ProductRepository

class GetProductsUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(): List<Product>{
        return repository.getProducts();
    }
}