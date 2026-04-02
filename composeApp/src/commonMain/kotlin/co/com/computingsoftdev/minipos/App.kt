package co.com.computingsoftdev.minipos


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import co.com.computingsoftdev.minipos.presentation.sales.sale_outcome.SaleOutcomeScreen
import co.com.computingsoftdev.minipos.presentation.sales.sale_outcome.SaleOutcomeViewModel
import ui.icons.AppIcons
import ui.navigation.NavBarIcon

@Composable
fun App(
    productViewModel: ProductViewModel,
    saleViewModel: SaleViewModel,
    saleOutcomeViewModel: SaleOutcomeViewModel
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
                AppBottomNavigation(
                    currentScreen = currentScreen,
                    onScreenSelected = { selected -> currentScreen = selected }
                )

                Spacer(modifier = Modifier.height(16.dp))
                when (currentScreen) {
                    Screen.Products -> ProductScreen(
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
                            saleViewModel.addItem(saleItem)
                            currentScreen = Screen.Sale
                        }
                    )

                    Screen.AddProduct -> ProductFormScreen(
                        productViewModel = productViewModel,
                        product = selectedProduct,
                        onBack = { currentScreen = Screen.Products }
                    )

                    Screen.Sale -> SaleScreen(
                        saleViewModel = saleViewModel,
                        productViewModel = productViewModel
                    )

                    Screen.SaleOutcome -> SaleOutcomeScreen(
                        saleOutcomeViewModel = saleOutcomeViewModel
                    )

                    Screen.Reports -> ReportScreen(
                        reportViewModel = reportViewModel,
                        onBack = { currentScreen = Screen.Sale }
                    )
                }


        }
    }
}

@Composable
fun AppBottomNavigation(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentScreen == Screen.Products,
            onClick = { onScreenSelected(Screen.Products) },
            icon = {
                NavBarIcon(
                    AppIcons.ListIcon,
                    "Products",
                    iconSize = 28.dp
                )
            }, // vacío por ahora
            label = { Text("Productos") }
        )
        NavigationBarItem(
            selected = currentScreen == Screen.Sale,
            onClick = { onScreenSelected(Screen.Sale) },
            icon = {
                NavBarIcon(
                    AppIcons.ShoppingCartIcon,
                    "Ventas",
                    iconSize = 28.dp
                )
            },
            label = { Text("Ventas") }
        )
        NavigationBarItem(
            selected = currentScreen == Screen.SaleOutcome,
            onClick = { onScreenSelected(Screen.SaleOutcome) },
            icon = {
                NavBarIcon(
                    AppIcons.CircleCheckIcon,
                    "Finalizadas",
                    iconSize = 28.dp
                )
            },
            label = { Text("Finalizadas") }
        )
        NavigationBarItem(
            selected = currentScreen == Screen.Reports,
            onClick = { onScreenSelected(Screen.Reports) },
            icon = {
                NavBarIcon(
                    AppIcons.ChartBarIcon,
                    "Reportes",
                    iconSize = 28.dp
                )
            },
            label = { Text("Reportes") }
        )
    }
}
