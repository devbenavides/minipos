package co.com.computingsoftdev.minipos.domain.usecase.product

import co.com.computingsoftdev.minipos.domain.model.Product
import co.com.computingsoftdev.minipos.domain.repository.ProductRepository

class AddProductUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(product: Product){
        repository.addProduct(product)
    }
}