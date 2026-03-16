package co.com.computingsoftdev.minipos.data.datasource.database

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import co.com.computingsoftdev.minipos.data.datasource.local.ProductLocalDataSource
import co.com.computingsoftdev.minipos.data.datasource.local.SaleLocalDataSource
import co.com.computingsoftdev.minipos.data.datasource.repository.ProductRepositoryImpl
import co.com.computingsoftdev.minipos.data.datasource.repository.SaleRepositoryImpl
import co.com.computingsoftdev.minipos.domain.usecase.product.AddProductUseCase
import co.com.computingsoftdev.minipos.domain.usecase.product.DeleteProductUseCase
import co.com.computingsoftdev.minipos.domain.usecase.product.GetProductsUseCase
import co.com.computingsoftdev.minipos.domain.usecase.product.UpdateProductUseCase
import co.com.computingsoftdev.minipos.domain.usecase.sale.SaveSaleUseCase
import co.com.computingsoftdev.minipos.presentation.products.ProductScreen
import co.com.computingsoftdev.minipos.presentation.products.ProductViewModel
import co.com.computingsoftdev.minipos.presentation.sales.SaleViewModel

fun main() = application {

    val databaseFactory = DatabaseFactory()

    val productLocal = ProductLocalDataSource(databaseFactory.productQueries)
    val saleLocal = SaleLocalDataSource(
        databaseFactory.saleQueries,
        databaseFactory.saleItemQueries,
        productLocal
    )

    val productRepository = ProductRepositoryImpl(productLocal)
    val saleRepository = SaleRepositoryImpl(saleLocal)

    val productViewModel = ProductViewModel(
        GetProductsUseCase(productRepository),
        AddProductUseCase(productRepository),
        UpdateProductUseCase(productRepository),
        DeleteProductUseCase(productRepository)
    )

    val saleViewModel = SaleViewModel(
        SaveSaleUseCase(saleRepository)
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "Mini POS"
    ) {
        MaterialTheme {
            Surface {
                ProductScreen(productViewModel)
            }
        }
    }
}