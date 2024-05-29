package view.home.columns

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import repository.implementation.neo4j.GraphNeo4jExporter
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import view.home.columns.dialog.DeleteGraphDialog
import view.home.columns.dialog.ExportGraphDialog
import view.settings.SettingsFileManager
import view.settings.SettingsScreen
import view.workspace.WorkspaceScreen
import viewModel.workspace.graph.GraphViewModel
import viewModel.workspace.graph.GraphsContainerViewModel
import viewModel.workspace.graph.utils.LocalDatabase
import kotlin.math.abs
import kotlinx.serialization.json.Json


@Composable
fun GraphRepresentation(viewModel: GraphViewModel, graphsContainerViewModel: GraphsContainerViewModel, titleColor: Color, subTitleColor: Color) {
    var isDeleteDialogShown by remember { mutableStateOf(false) }
    var isExportDialogShown by remember { mutableStateOf(false)}
    val navigator = LocalNavigator.currentOrThrow
    val density = LocalDensity.current.density
    val colorTriples = listOf(
        Triple(Color(0xFF6200EE), Color(0xFFB07FF6), Color.White),
        Triple(Color(0xFFF44336), Color(0xFFFAB3AE), Color.White),
        Triple(Color(0xFF38761d), Color(0xFF9BBA8E), Color.White),
        Triple(Color(0xFFC90076), Color(0xFFE999C8), Color.White),
        Triple(Color(0xFF16537E), Color(0xFFD0DCE5), Color.White),
    )
    val colorTriple = colorTriples[abs(viewModel.graphName.hashCode()) % colorTriples.size]

    fun createLinearGradient(): Brush {
        return Brush.linearGradient(
            colors = listOf(colorTriple.first, colorTriple.second),
            start = Offset(0f, 0f),
            end = Offset(50f*density, 50f*density)
        )
    }

    var iconBgColors: Brush by mutableStateOf(createLinearGradient())
    var iconTextColor: Color by mutableStateOf(colorTriple.third)


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clip(shape = MaterialTheme.shapes.small)
                .weight(1f)
                .clickable {
                    navigator.push(WorkspaceScreen(viewModel))
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                modifier = Modifier
                    .size(40.dp),
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                        .background(brush = iconBgColors)
                ) {
                    Text(
                        text = viewModel.graphName.slice(0..1).uppercase(),
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = iconTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Bottom),

                ) {
                Text(
                    text = viewModel.graphName,
                    color = titleColor,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )

                Text(
                    text = "${LocalDatabase.getGraphsDirectory()}/${viewModel.graphName}",
                    color = subTitleColor,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize
                )
            }
        }

//        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.padding(4.dp).alpha(.5f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = if (viewModel.isDirected()) "directed" else "undirected",
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                textAlign = TextAlign.End
            )
            Text(
                text = if (viewModel.isWeighted()) "weighted" else "unweighted",
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                textAlign = TextAlign.End
            )
        }

        IconButton(
            modifier = Modifier,
            onClick = {
                isDeleteDialogShown = !isDeleteDialogShown
            }
        ) {
            Icon(
                Icons.Filled.DeleteForever, "deleteIcon",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        IconButton(
            modifier = Modifier,
            onClick = {
                isExportDialogShown = !isExportDialogShown
            }
        ) {
            Icon(
                Icons.Filled.Publish, "importIcon",
                tint = MaterialTheme.colorScheme.primary
            )
        }

    }

    if (isDeleteDialogShown) {
        DeleteGraphDialog(
            onDismissRequest = {
                isDeleteDialogShown = !isDeleteDialogShown
            },
            onConfirmRequest = {
                graphsContainerViewModel.deleteGraph(viewModel.graphName)
                isDeleteDialogShown = !isDeleteDialogShown
            }
        )
    }

    if (isExportDialogShown) {
        ExportGraphDialog(
            onDismissRequest = {
                isExportDialogShown = !isExportDialogShown
            },
            onSettingsRequest = {
                isExportDialogShown = !isExportDialogShown
                navigator.push(SettingsScreen())
            },
            onNeo4jRequest = {
                isExportDialogShown = !isExportDialogShown
                val settingsManager = SettingsFileManager(".settings.json")
                val settingsJson = settingsManager.loadSettings()

                val settingsObject = Json.decodeFromString<SettingsFileManager.settings>(settingsJson)

                val neo4jUrl = settingsObject.neo4jUrl
                val neo4jUser = settingsObject.neo4jUser
                val neo4jPassword = settingsObject.neo4jPassword

                val neo4jExporter = GraphNeo4jExporter()
//                neo4jExporter.exportGraph(viewModel., listOf(neo4jUrl, neo4jUser, neo4jPassword), viewModel.graphName)
            },
            onSQLiteRequest = {
                isExportDialogShown = !isExportDialogShown
                val settingsManager = SettingsFileManager(".settings.json")
                val settingsJson = settingsManager.loadSettings()

                val settingsObject = Json.decodeFromString<SettingsFileManager.settings>(settingsJson)

                val sqlitePath = settingsObject.sqlitePath
//                val sqlite
            }
        )
    }
}


