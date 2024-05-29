package view.home.columns.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ExportGraphDialog(onDismissRequest: () -> Unit, onNeo4jRequest: () -> Unit, onSQLiteRequest: () -> Unit, onSettingsRequest: () -> Unit) {
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
                    text = "Export Graph"
                )
                Text(
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 24.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    text = "Make sure to check settings of the app first. In neo4j, the graph's name will serve as a unique ID that you will have to remember!!",
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
                            onNeo4jRequest()
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
