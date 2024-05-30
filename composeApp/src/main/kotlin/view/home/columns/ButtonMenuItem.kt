package view.home.columns

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun ButtonMenuItem(text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick
    ) {
        Text(
            text = text,
        )
    }
}
