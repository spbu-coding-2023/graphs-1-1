package view.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun NumberRangePicker(numberRange: IntRange, text: String, onChange: (Int) -> Unit) {
    var currentNumber by remember { mutableStateOf(numberRange.first) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(
            text = "$text: $currentNumber",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize
        )

        Spacer(modifier = Modifier.weight(1f))

        FilledIconButton(
            onClick = {
                var nextNumber = currentNumber - 1
                if (nextNumber < numberRange.first) nextNumber = numberRange.first
                currentNumber = nextNumber
                onChange(nextNumber)
            }
        ) {
            Icon(Icons.Filled.Remove, "decIcon")
        }

        FilledIconButton(
            onClick = {
                var nextNumber = currentNumber + 1
                if (nextNumber > numberRange.last) nextNumber = numberRange.last
                currentNumber = nextNumber
                onChange(nextNumber)
            }
        ) {
            Icon(Icons.Filled.Add, "incIcon")
        }
    }
}