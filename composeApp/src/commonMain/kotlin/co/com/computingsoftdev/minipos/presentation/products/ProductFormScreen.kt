package co.com.computingsoftdev.minipos.presentation.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import co.com.computingsoftdev.minipos.domain.model.Product

@Composable
fun ProductFormScreen(
    productViewModel: ProductViewModel,
    product: Product? = null,
    onBack: () -> Unit
) {

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    LaunchedEffect(product) {
        name = product?.name ?: ""
        price = product?.price?.toString() ?: ""
        description = product?.description ?: ""
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = if(product == null) "Nuevo Producto" else "Editar Producto",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val priceLong = price.toLongOrNull()

                if (name.isBlank() || priceLong == null) {
                    return@Button
                }

                if(product == null){
                    productViewModel.addProduct(
                        name = name,
                        price = priceLong,
                        description = description
                    )
                }else{
                    productViewModel.updateProduct(
                        product.copy(
                            name = name,
                            price = priceLong,
                            description = description
                        )
                    )
                }
                onBack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (product == null) "Guardar" else "Actualizar")
        }
        Spacer(modifier = Modifier.height(8.dp))

        // 🔙 Cancelar
        TextButton(
            onClick = { onBack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
    }
}
