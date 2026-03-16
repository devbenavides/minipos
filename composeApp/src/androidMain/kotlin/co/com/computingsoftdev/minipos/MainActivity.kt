package co.com.computingsoftdev.minipos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import co.com.computingsoftdev.minipos.data.datasource.database.DatabaseFactory
import co.com.computingsoftdev.minipos.data.datasource.local.ProductLocalDataSource
import co.com.computingsoftdev.minipos.data.datasource.local.SaleLocalDataSource
import co.com.computingsoftdev.minipos.data.datasource.repository.ProductRepositoryImpl
import co.com.computingsoftdev.minipos.data.datasource.repository.SaleRepositoryImpl
import co.com.computingsoftdev.minipos.domain.usecase.product.*
import co.com.computingsoftdev.minipos.domain.usecase.sale.*
import co.com.computingsoftdev.minipos.presentation.products.ProductViewModel
import co.com.computingsoftdev.minipos.presentation.sales.SaleViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val databaseFactory = DatabaseFactory(this)

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
            AddItemToSaleUseCase(),
            CalculateSubtotalUseCase(),
            CalculateTotalUseCase(),
            SaveSaleUseCase(saleRepository)
        )

        setContent {
            App(
                productViewModel = productViewModel,
                saleViewModel = saleViewModel
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    MaterialTheme {
        Text("Mini POS Preview")
    }
}
