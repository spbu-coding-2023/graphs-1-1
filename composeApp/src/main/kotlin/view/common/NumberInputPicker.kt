package view.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun NumberInputPicker(text: String, onChange: (Int?) -> Unit) {
    val pattern = remember { Regex("^\\d+\$") }

    var currentValue by remember { mutableStateOf<Int?>(null) }
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "$text:",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
        )

        TextField(
            value = if (currentValue == null) "" else currentValue.toString(),
            onValueChange = {
                if (it.isEmpty() || !it.matches(pattern) || it.length > 8) {
                    currentValue = null
                } else {
                    currentValue = it.toInt()
                }
                onChange(currentValue)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp)
        )
    }
}

