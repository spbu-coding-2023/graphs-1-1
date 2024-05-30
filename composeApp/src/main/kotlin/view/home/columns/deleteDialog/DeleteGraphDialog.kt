package view.home.columns.deleteDialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DeleteGraphDialog(onDismissRequest: () -> Unit, onConfirmRequest: () -> Unit) {
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
                    text = "Delete Graph"
                )
                Text(
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 24.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    text = "Are you sure you want to delete this graph forever?",
                )

                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { onDismissRequest() },
                    ) {
                        Text("Cancel")
                    }

                    OutlinedButton(
                        onClick = {
                            onConfirmRequest()
                        },
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}
