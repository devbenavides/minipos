package co.com.computingsoftdev.minipos


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.com.computingsoftdev.minipos.presentation.products.ProductFormScreen
import co.com.computingsoftdev.minipos.presentation.products.ProductScreen
import co.com.computingsoftdev.minipos.presentation.products.ProductViewModel
import co.com.computingsoftdev.minipos.presentation.sales.SaleScreen
import co.com.computingsoftdev.minipos.presentation.sales.SaleViewModel

@Composable
fun App(
    productViewModel: ProductViewModel,
    saleViewModel: SaleViewModel
) {

    var currentScreen by remember { mutableStateOf("products") }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { currentScreen = "products" }) { Text("Productos") }
                Button(onClick = { currentScreen = "sale" }) { Text("Nueva Venta") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (currentScreen) {
                "products" -> {
                    // Muestra la lista y permite agregar productos
                    ProductScreen(productViewModel = productViewModel, saleViewModel = saleViewModel) {
                        // callback para ir al formulario
                        currentScreen = "add_product"
                    }
                }

                "add_product" -> {
                    ProductFormScreen(viewModel = productViewModel) {
                        // callback para volver a la lista
                        currentScreen = "products"
                    }
                }

                "sale" -> {
                    SaleScreen(saleViewModel)
                }
            }
        }
    }
}