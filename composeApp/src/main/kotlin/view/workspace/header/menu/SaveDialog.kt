package view.workspace.header.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun SaveDialog(onDismissRequest: () -> Unit, onSaveRequest: () -> Unit, onNotSaveRequest: () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest,
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
                    text = "Save"
                )
                Text(
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 24.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    text = "Do you want to save your graph before you go?",
                )

                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                ) {
                    OutlinedButton(
                        onClick = {
                            onNotSaveRequest()
                        },
                    ) {
                        Text("Don't save")
                    }

                    Button(
                        onClick = {
                            onSaveRequest()
                        },
                    ) {
                        Text("Save")
                    }

                    Button(
                        onClick = {
                            onDismissRequest()
                        },
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}