package co.com.computingsoftdev.minipos.presentation.products

import co.com.computingsoftdev.minipos.domain.model.Product

data class ProductUiState(
    val products: List<Product> = emptyList(),

    val isLoading: Boolean = false,

    val error: String? = null
)

