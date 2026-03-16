package co.com.computingsoftdev.minipos.presentation.products

import co.com.computingsoftdev.minipos.domain.model.Product
import co.com.computingsoftdev.minipos.domain.usecase.product.AddProductUseCase
import co.com.computingsoftdev.minipos.domain.usecase.product.DeleteProductUseCase
import co.com.computingsoftdev.minipos.domain.usecase.product.GetProductsUseCase
import co.com.computingsoftdev.minipos.domain.usecase.product.UpdateProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ProductViewModel(
    private val getProducts: GetProductsUseCase,
    private val addProduct: AddProductUseCase,
    private val updateProduct: UpdateProductUseCase,
    private val deleteProduct: DeleteProductUseCase
) {
    private val _uiState = MutableStateFlow(ProductUiState())

    val uiState: StateFlow<ProductUiState> = _uiState

    fun loadProducts() {

        val products = getProducts()

        _uiState.update {
            it.copy(products = products)
        }
    }

    fun addProduct(name: String, price: Long, description: String?) {

        val product = Product(
            id = 0,
            name = name,
            price = price,
            description = description
        )

        addProduct(product)

        loadProducts()
    }

    fun updateProduct(product: Product) {

        updateProduct.invoke(product)

        loadProducts()
    }

    fun deleteProduct(id: Long) {

        deleteProduct.invoke(id)

        loadProducts()
    }
}