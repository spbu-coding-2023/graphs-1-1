package view.home.columns.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ImportGraphDialog(onDismissRequest: () -> Unit, onNeo4jRequest: (String) -> Unit, onSQLiteRequest: () -> Unit, onSettingsRequest: () -> Unit) {
    var graphId by remember { mutableStateOf(TextFieldValue("")) }
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
                    text = "Import Graph"
                )
                Text(
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 24.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    text = "Make sure to check settings of the app first. For neo4j, also provide the name of the graph.",
                )

                OutlinedTextField(
                    value = graphId,
                    onValueChange = {
                        graphId = it
                    },
                    label = { Text("NEO4J ONLY: Enter the name of the graph") },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
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

                    Button(
                        onClick = { onSettingsRequest() },
                    ) {
                        Text("Settings")
                    }

                    OutlinedButton(
                        onClick = {
                            onNeo4jRequest(graphId.text)
                        },
                    ) {
                        Text("Neo4j")
                    }
                    OutlinedButton(
                        onClick = {
                            onSQLiteRequest()
                        }
                    ) {
                        Text("SQLite")
                    }

                }
            }
        }
    }
}
