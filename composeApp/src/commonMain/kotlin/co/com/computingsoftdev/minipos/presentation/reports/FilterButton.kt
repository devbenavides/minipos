package co.com.computingsoftdev.minipos.presentation.reports

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FilterButton(
    text: String,
    selected: String,
    onClick: () -> Unit
) {
    val isSelected = text == selected

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.secondary
        )
    ) {
        Text(text)
    }
}