package co.com.computingsoftdev.minipos.presentation.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ProductFormScreen(
    viewModel: ProductViewModel,
    onSaved: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Precio") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            viewModel.addProduct(
                name = name,
                price = price.toLongOrNull() ?: 0L,
                description = description.takeIf { it.isNotBlank() }
            )
            name = ""
            price = ""
            description = ""
            onSaved() // Volver a la lista
        }) {
            Text("Guardar Producto")
        }
    }
}
