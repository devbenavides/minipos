package co.com.computingsoftdev.minipos.presentation.sales

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.com.computingsoftdev.minipos.domain.model.Product
import co.com.computingsoftdev.minipos.domain.model.Sale
import co.com.computingsoftdev.minipos.domain.model.SaleItem
import co.com.computingsoftdev.minipos.domain.model.SaleStatus
import co.com.computingsoftdev.minipos.presentation.products.ProductViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.buttons.IconButtonFilled
import ui.icons.AppIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleScreen(
    saleViewModel: SaleViewModel,
    productViewModel: ProductViewModel
) {
    val saleUiState by saleViewModel.uiState.collectAsState()

    val currentSale = saleUiState.currentSale
    val cartItems = currentSale?.items ?: emptyList()
    val subtotal = saleUiState.subtotal
    val total = saleUiState.total
    val pendingSales = saleUiState.pendingSales

    var showProductSheet by remember { mutableStateOf(false) }
    var showPendingSheet by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .padding(8.dp)
        ) {

            //Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Carrito de Venta",
                    style = MaterialTheme.typography.headlineSmall
                )

                currentSale?.let { sale ->
                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = "Venta #${sale.id} • ${sale.items.size} items",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            //Carrito (más grande ahora)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    if (cartItems.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No hay productos en el carrito", color = Color.Gray)
                            }
                        }
                    } else {
                        items(
                            items = cartItems,
                            key = { it.productId }
                        ) { item ->

                            //Swipe para eliminar
                            SwipeToDismissBox(
                                state = rememberSwipeToDismissBoxState(
                                    confirmValueChange = {
                                        if (it == SwipeToDismissBoxValue.EndToStart) {
                                            saleViewModel.removeItem(item.productId)
                                            true
                                        } else false
                                    }
                                ),
                                backgroundContent = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(8.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(MaterialTheme.colorScheme.error),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Icon(
                                            imageVector = AppIcons.TrashIcon,
                                            contentDescription = "Eliminar",
                                            tint = Color.White,
                                            modifier = Modifier.padding(end = 16.dp)
                                        )
                                    }
                                },
                                content = {
                                    SaleItemRow(
                                        item = item,
                                        onQuantityChange = {
                                            saleViewModel.updateItemQuantity(
                                                item.productId,
                                                it
                                            )
                                        },
                                        onRemove = {
                                            saleViewModel.removeItem(item.productId)
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            //Totales
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Subtotal: $subtotal")
                    Text(
                        "Total: $total",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            //Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                //Lista de Productos
                IconButtonFilled(
                    onClick = { showProductSheet = true },
                    icon = AppIcons.ListIcon,
                    text = "",
                    modifier = Modifier.weight(1f)
                )

                //Completar Venta
                IconButtonFilled(
                    onClick = { saleViewModel.saveSale() },
                    icon = AppIcons.CheckIcon,
                    text = "",
                    enabled = cartItems.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                )

                //Nueva venta
                IconButtonFilled(
                    onClick = { saleViewModel.startNewSale() },
                    icon = AppIcons.NotesIcon,
                    text = "",
                    modifier = Modifier.weight(1f)
                )

                //Ventas Pendientes
                IconButtonFilled(
                    onClick = { showPendingSheet = true },
                    icon = AppIcons.ListIcon,
                    text = "",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    if(showProductSheet){
        ProductBottomSheet(
            products = productViewModel.uiState.value.products,
            onSelect = {
                    product ->
                currentSale?.let { sale ->
                    saleViewModel.addItem(
                        SaleItem(
                            id = 0L,
                            saleId = sale.id,
                            productId = product.id,
                            productName = product.name,
                            price = product.price,
                            quantity = 1
                        )
                    )

                }
            },
            onDismiss = {showProductSheet=false},
            sheetState = sheetState
        )
    }

    //BottomSheet ventas pendientes
    if (showPendingSheet) {
        PendingSalesBottomSheet(
            pendingSales = pendingSales,
            currentSaleId = currentSale?.id,
            onSelect = {
                saleViewModel.selectPendingSale(it)
                showPendingSheet = false
            },
            onDelete = { saleViewModel.cancelSale(it) },
            onDismiss = { showPendingSheet = false },
            sheetState = sheetState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingSalesBottomSheet(
    pendingSales: List<Sale>,
    currentSaleId: Long?,
    onSelect: (Sale) -> Unit,
    onDelete: (Sale) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(16.dp)
        ) {
            Text(
                "Ventas Pendientes",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn {
                items(pendingSales, key = { it.id }) { sale ->
                    val isSelected = sale.id == currentSaleId
                    val isEmpty = sale.items.isEmpty()
                    val total = sale.items.sumOf { it.subtotal() }

                    val backgroundColor = MaterialTheme.colorScheme.surface

                    val borderModifier = if (isSelected)
                        Modifier.border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(12.dp)
                        )
                    else Modifier

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .then(borderModifier)
                            .clickable { onSelect(sale) },
                        colors = CardDefaults.cardColors(containerColor = backgroundColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Venta #${sale.id}")
                                Text("Items: ${sale.items.size}")
                                Text("Total: $total")
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isEmpty) {
                                        Button(
                                            onClick = { onDelete(sale) },
                                            modifier = Modifier.height(40.dp),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                                                contentColor = MaterialTheme.colorScheme.error
                                            )
                                        ) {
                                            Icon(
                                                AppIcons.TrashIcon,
                                                contentDescription = "Eliminar",
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Eliminar")
                                        }
                                    } else {
                                        Button(
                                            onClick = { onDelete(sale) },
                                            modifier = Modifier.height(40.dp),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        ) {
                                            Icon(
                                                AppIcons.XIcon,
                                                contentDescription = "Cancelar",
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Cancelar")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Composable para un item del carrito
@Composable
fun SaleItemRow(
    item: SaleItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 88.dp) // 🔥 más alto tipo e-commerce
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = AppIcons.NotesIcon, // placeholder
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            //Info del producto
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.productName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Unit: $${item.price}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 4.dp)
                ) {
                    IconButton(
                        modifier = Modifier.size(36.dp),
                        onClick = {
                            if (item.quantity > 1) {
                                onQuantityChange(item.quantity - 1)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = AppIcons.MinusIcon,
                            contentDescription = "Disminuir"
                        )
                    }

                    //Animación de cantidad
                    AnimatedContent(
                        targetState = item.quantity,
                        label = "quantity_anim"
                    ) { qty ->
                        Text(
                            text = qty.toString(),
                            modifier = Modifier.padding(horizontal = 6.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    IconButton(
                        modifier = Modifier.size(36.dp),
                        onClick = { onQuantityChange(item.quantity + 1) }
                    ) {
                        Icon(
                            imageVector = AppIcons.PlusIcon,
                            contentDescription = "Aumentar"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Precio + eliminar
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(64.dp)
            ) {

                //Subtotal destacado
                Text(
                    text = "$${item.subtotal()}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                //Eliminar
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = AppIcons.TrashIcon,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductBottomSheet(
    products: List<Product>,
    onSelect: (Product) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState
) {
    var searchQuery by remember { mutableStateOf("") }
    var recentlyAddedId by remember { mutableStateOf<Long?>(null) }
    val scope = rememberCoroutineScope()

    // Filtrar productos por búsqueda
    val filteredProducts = products.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(16.dp)
        ) {
            // Título
            Text(
                "Selecciona un producto",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Buscador
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar producto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(AppIcons.SearchIcon, contentDescription = "Buscar") }
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Lista de productos
            LazyColumn {
                items(filteredProducts, key = { it.id }) { product ->
                    // Borde temporal si fue agregado
                    val borderColor = if (recentlyAddedId == product.id)
                        MaterialTheme.colorScheme.primary
                    else
                        Color.Transparent

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .border(
                                width = 2.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                onSelect(product)
                                recentlyAddedId = product.id

                                // Limpiar el borde después de 1.5s
                                scope.launch {
                                    delay(1500)
                                    recentlyAddedId = null
                                }
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        var expanded by remember { mutableStateOf(false) }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = product.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Precio: $${product.price}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                // Indicador visual pequeño de agregado
                                if (recentlyAddedId == product.id) {
                                    Icon(
                                        imageVector = AppIcons.CheckIcon,
                                        contentDescription = "Agregado",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                TextButton(
                                    onClick = { expanded = !expanded },
                                    enabled = product.description?.isNotBlank() == true
                                ) {
                                    Text(
                                        if (product.description?.isNotBlank() == true) {
                                            if (expanded) "Ocultar" else "Ver descripción"
                                        } else {
                                            "Sin descripción"
                                        }
                                    )
                                }
                            }

                            // Descripción expandible
                            if (expanded) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = product.description ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
