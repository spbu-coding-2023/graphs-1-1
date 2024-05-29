package view.home.columns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import graph.implementation.UndirectedUnweightedGraph
import kotlinx.serialization.json.Json
import repository.implementation.neo4j.GraphNeo4jImporter
import repository.implementation.sqlite.GraphSQLiteImporter
import view.home.columns.dialog.ImportGraphDialog
import view.settings.SettingsFileManager
import view.settings.SettingsScreen
import viewModel.workspace.graph.GraphsContainerViewModel
import org.neo4j.driver.*
import java.io.File

@Composable
fun ColumnGraphsList(modifier: Modifier, graphsContainerViewModel: GraphsContainerViewModel) {
    val graphsContainer by graphsContainerViewModel.graphsContainer.collectAsState()
    var searchText by remember { mutableStateOf("") }
    var isImportDialogShown by remember { mutableStateOf(false)}
    val navigator = LocalNavigator.currentOrThrow

    Column {
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.background).padding(8.dp, 16.dp)
        ) {
           OutlinedTextField(
               value = searchText,
               onValueChange = {
                   searchText = it
               },
               label = { Text("Search") },
               modifier = Modifier.padding(4.dp).fillMaxWidth()
           )
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .clip(shape = MaterialTheme.shapes.large.copy(
                    topEnd = CornerSize(0),
                    bottomEnd = CornerSize(0),
                    bottomStart = CornerSize(0),
                ))
                .verticalScroll(rememberScrollState())
                .then(modifier),
            tonalElevation = 2.dp,
        ) {
            Column(
                modifier = Modifier.padding(0.dp, 8.dp).fillMaxHeight()
            ) {
                val listedGraphs = graphsContainer.filter { g -> g.graphName.lowercase().startsWith(searchText.lowercase()) }
                if (listedGraphs.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "no graphs are found",
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                        )
                    }
                } else {
                    listedGraphs.forEach { graph ->
                        GraphRepresentation(
                            viewModel = graph,
                            graphsContainerViewModel = graphsContainerViewModel,
                            titleColor = MaterialTheme.colorScheme.primary,
                            subTitleColor = MaterialTheme.colorScheme.secondary
                        )

                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                HorizontalDivider(modifier = Modifier.padding(16.dp, 8.dp))

                TextButton(
                    onClick = {
                        isImportDialogShown = !isImportDialogShown
                    },
                    modifier = Modifier.padding(16.dp, 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
                    ) {
                        Icon(Icons.Default.Add, "addIcon")
                        Text("Import existing graph")
                    }
                }
            }
        }
    }
    if (isImportDialogShown) {
        ImportGraphDialog(
            onDismissRequest = {
                isImportDialogShown = !isImportDialogShown
            },
            onSettingsRequest = {
                isImportDialogShown = !isImportDialogShown
                navigator.push(SettingsScreen())
            },
            onNeo4jRequest = {
                isImportDialogShown = !isImportDialogShown
                val settingsManager = SettingsFileManager()
                val settingsJson = settingsManager.loadSettings()

                val settingsObject = Json.decodeFromString<SettingsFileManager.settings>(settingsJson)

                val neo4jUrl = settingsObject.neo4jUrl
                val neo4jUser = settingsObject.neo4jUser
                val neo4jPassword = settingsObject.neo4jPassword

                val driverFactory: (String, AuthToken) -> Driver = { currentUri, authToken ->
                    GraphDatabase.driver(currentUri, authToken)
                }
                val neo4jImporter = GraphNeo4jImporter(driverFactory)
                val graph = neo4jImporter.importGraph<String, String>(it, listOf(neo4jUrl, neo4jUser, neo4jPassword))
            },
            onSQLiteRequest = {
                isImportDialogShown = !isImportDialogShown
                val settingsManager = SettingsFileManager()
                val settingsJson = settingsManager.loadSettings()

                val settingsObject = Json.decodeFromString<SettingsFileManager.settings>(settingsJson)

                val sqlitePath = File(settingsObject.sqlitePath)
                val sqLiteImporter = GraphSQLiteImporter()
                val graph = UndirectedUnweightedGraph<String, String>()
                sqLiteImporter.importGraph(graph, sqlitePath)

            }
        )
    }
}

