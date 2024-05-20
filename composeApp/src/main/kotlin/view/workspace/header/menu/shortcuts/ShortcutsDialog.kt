package view.workspace.header.menu.shortcuts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ShortcutsDialog(onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    text = "Shortcuts"
                )
                Text(
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 24.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    text = "No shortcuts for now",
                )

                TextButton(
                    onClick = { onDismissRequest() },
                ) {
                    Text("Close")
                }
            }
        }
    }
}
