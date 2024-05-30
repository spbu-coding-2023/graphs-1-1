package view.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp

@Composable
fun <T>OptionChoseRadio(chooses: List<Pair<T, String>>, onChoseChange: (T) -> Unit) {
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(chooses[0]) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        chooses.forEach { chose ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedOption == chose,
                        onClick = {
                            onOptionSelected(chose)
                            onChoseChange(chose.first)
                        }
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (chose == selectedOption),
                    onClick = {
                        onOptionSelected(chose)
                        onChoseChange(chose.first)
                    }
                )
                Text(
                    text = chose.second,
                    style = MaterialTheme.typography.bodyMedium.merge(),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}