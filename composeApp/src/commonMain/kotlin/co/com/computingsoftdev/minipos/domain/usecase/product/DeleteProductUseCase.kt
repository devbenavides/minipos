package co.com.computingsoftdev.minipos.domain.usecase.product

import co.com.computingsoftdev.minipos.domain.model.Product
import co.com.computingsoftdev.minipos.domain.repository.ProductRepository

class DeleteProductUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(id: Long){
        return repository.deleteProduct(id)
    }
}