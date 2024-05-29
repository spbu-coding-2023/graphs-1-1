package view.settings

import view.common.CommonScreenLayout
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import viewModel.workspace.graph.utils.LocalDatabase

class SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var showSQLiteInput by remember { mutableStateOf(true) }
        var showNeo4jInputs by remember { mutableStateOf(false) }

        var sqlitePath by remember { mutableStateOf(TextFieldValue("")) }
        var neo4jUrl by remember { mutableStateOf(TextFieldValue("")) }
        var neo4jUser by remember { mutableStateOf(TextFieldValue("")) }
        var neo4jPassword by remember { mutableStateOf(TextFieldValue("")) }


        var savedSqlitePath by remember { mutableStateOf("") }
        var savedNeo4jUrl by remember { mutableStateOf("") }
        var savedNeo4jUser by remember { mutableStateOf("") }
        var savedNeo4jPassword by remember { mutableStateOf("") }

        CommonScreenLayout("Settings") {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    OutlinedButton(
                        onClick = {
                        showSQLiteInput = true
                        showNeo4jInputs = false
                    }, modifier = Modifier.weight(1f).padding(8.dp)) {
                        Text("SQLite")
                    }
                    OutlinedButton(onClick = {
                        showSQLiteInput = false
                        showNeo4jInputs = true
                    }, modifier = Modifier.weight(1f).padding(8.dp)) {
                        Text("neo4j")
                    }
                }

                if (showSQLiteInput) {
                    OutlinedTextField(
                        value = sqlitePath,
                        onValueChange = {
                            sqlitePath = it

                        },
                        label = { Text("Please enter path to the local database") },
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    )
                }

                if (showNeo4jInputs) {
                    OutlinedTextField(
                        value = neo4jUrl,
                        onValueChange = {
                            neo4jUrl = it
                        },
                        label = { Text("Please enter URL") },
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    )
                    OutlinedTextField(
                        value = neo4jUser,
                        onValueChange = {
                            neo4jUser = it
                        },
                        label = { Text("Please enter user name") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                    OutlinedTextField(
                        value = neo4jPassword,
                        onValueChange = {
                            neo4jPassword = it
                        },
                        label = { Text("Please enter password") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Button(
                        onClick = { navigator.pop() },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "backIcon")
                        Text("Back")
                    }
                    Button(
                        onClick = {
                            val settings = """
                                {
                                    "sqlitePath": "${sqlitePath.text}",
                                    "neo4jUrl": "${neo4jUrl.text}",
                                    "neo4jUser": "${neo4jUser.text}",
                                    "neo4jPassword": "${neo4jPassword.text}"
                                }
                            """.trimIndent()
                            val settingsManager = SettingsFileManager("settings.json")
                            settingsManager.saveSettings(settings)
                        }
                    ) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}
