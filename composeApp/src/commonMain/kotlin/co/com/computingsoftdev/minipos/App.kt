package co.com.computingsoftdev.minipos


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.com.computingsoftdev.minipos.data.datasource.local.SaleLocalDataSource
import co.com.computingsoftdev.minipos.domain.model.Product
import co.com.computingsoftdev.minipos.domain.usecase.reports.GetCompletedSalesReportUseCase
import co.com.computingsoftdev.minipos.presentation.navigation.Screen
import co.com.computingsoftdev.minipos.presentation.products.ProductFormScreen
import co.com.computingsoftdev.minipos.presentation.products.ProductScreen
import co.com.computingsoftdev.minipos.presentation.products.ProductViewModel
import co.com.computingsoftdev.minipos.presentation.reports.ReportScreen
import co.com.computingsoftdev.minipos.presentation.reports.ReportViewModel
import co.com.computingsoftdev.minipos.presentation.sales.SaleScreen
import co.com.computingsoftdev.minipos.presentation.sales.SaleViewModel

@Composable
fun App(
    productViewModel: ProductViewModel,
    saleViewModel: SaleViewModel
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Products) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    val reportViewModel = remember {
        ReportViewModel(
            GetCompletedSalesReportUseCase(saleViewModel.getSalesLocalDataSource())
        )
    }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { currentScreen = Screen.Products }) { Text("Productos") }
                Button(onClick = { currentScreen = Screen.Sale }) { Text("Ventas") }
                Button(onClick = { currentScreen = Screen.Reports }) { Text("Reportes") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (currentScreen) {
                Screen.Products -> {
                    // Muestra la lista y permite agregar productos
                    ProductScreen(
                        productViewModel = productViewModel,
                        saleViewModel = saleViewModel,
                        onAddProductClick = {
                            selectedProduct = null
                            currentScreen = Screen.AddProduct
                        },
                        onEditProductClick = {
                            selectedProduct = it
                            currentScreen = Screen.AddProduct
                        },
                        onAddProduct = { saleItem ->
                            saleViewModel.addItem(saleItem) // agrega a la venta actual
                            currentScreen = Screen.Sale     // opcional: navegar a la venta
                        }
                    )
                }

                Screen.AddProduct -> {
                    ProductFormScreen(
                        productViewModel = productViewModel,
                        product = selectedProduct,
                        onBack = {
                            currentScreen = Screen.Products
                        }
                    )
                }

                Screen.Sale -> { // ✅ este es el branch correcto
                    SaleScreen(
                        saleViewModel = saleViewModel,
                        productViewModel = productViewModel

                    )
                }

                Screen.Reports -> {
                    ReportScreen(
                        reportViewModel = reportViewModel,
                        onBack = { currentScreen = Screen.Sale } // vuelve a ventas
                    )
                }
            }
        }
    }
}