package view.workspace.graphConfiguration

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import display.keyVertex.implementation.GraphBetweennessCentrality
import display.minimumSpanningTree.implementation.GraphMSTWithKruskal
import display.pathSearch.implementation.GraphDijkstraPathFinder
import display.pathSearch.implementation.GraphPathSearchBellmanFord
import display.placement.implementation.GraphPlacementRandom
import display.placement.implementation.GraphPlacementYifanHu
import kotlinx.coroutines.flow.filter
import model.VertexModel
import view.common.NumberInputPicker
import view.common.NumberRangePicker
import viewModel.workspace.graph.GraphViewModel

private data class GraphRunnableOption(val title: String, val description: String, val onRun: () -> Unit, val content: (@Composable () -> Unit)? = null)

@Composable
fun GraphConfiguration(viewModel: GraphViewModel, modifier: Modifier) {
    var isShown by rememberSaveable { mutableStateOf(true) }
    var isRunning by remember { mutableStateOf(false) }
    var keyVertexIncrease by remember { mutableStateOf(1) }
    var selectedVertexStartId by remember { mutableStateOf<Int?>(null) }
    var selectedVertexEndId by remember { mutableStateOf<Int?>(null) }

    val runnableOptions = listOf(
        GraphRunnableOption(
            title = "Placement Yifan Hu",
            description = "runs Yifan Hu algorithm",
            onRun = {
                isRunning = true
                val graphPlacement = GraphPlacementYifanHu()
                graphPlacement.setSize(4, 4)
                viewModel.runPlacement(graphPlacement, { isRunning = false })
            }
        ),
        GraphRunnableOption(
            title = "Placement Random",
            description = "places graph randomly",
            onRun = {
                isRunning = true
                val graphPlacement = GraphPlacementRandom()
                graphPlacement.setSize(600, 600)
                viewModel.runPlacement(graphPlacement, { isRunning = false })
            }
        ),
        GraphRunnableOption(
            title = "Key Vertex",
            description = "Run betweenness centrality algorithm",
            onRun = {
                isRunning = true
                val graphBC = GraphBetweennessCentrality()
                viewModel.runKeyVertex(graphBC, keyVertexIncrease.toFloat(), { isRunning = false })
            },
            content = {
                NumberRangePicker(1..4, "Increase", { inc -> keyVertexIncrease = inc })
            }
        ),
        GraphRunnableOption(
            title = "Find path Bellman-Ford",
            description = "Find shortest path with Bellman-Ford algorithm between two vertices",
            onRun = {
                val vertexStartId = selectedVertexStartId
                val vertexEndId = selectedVertexEndId
                if (vertexStartId == null || vertexEndId == null) return@GraphRunnableOption
                val graphVertices = viewModel.vertices.value
                val vertexStart = graphVertices.find { it.id == vertexStartId }
                val vertexEnd = graphVertices.find { it.id == vertexEndId }
                if (vertexStart == null || vertexEnd == null) return@GraphRunnableOption
                val graphShortestPath = GraphPathSearchBellmanFord()
                isRunning = true
                viewModel.runResetEdges()
                viewModel.runShortestPath(graphShortestPath, vertexStart, vertexEnd, { isRunning = false })
            },
            content = {
                NumberInputPicker(
                    text = "starting vertex id",
                    onChange = {
                        it?.let { selectedVertexStartId = it }
                    }
                )
                NumberInputPicker(
                    text = "ending vertex id",
                    onChange = {
                        it?.let { selectedVertexEndId = it }
                    }
                )
            }
        ),
        GraphRunnableOption(
            title = "Find path Dijkstra",
            description = "Find shortest path with Dijkstra algorithm between two vertices",
            onRun = {
                if (!viewModel.isEdgesPositive()) return@GraphRunnableOption
                val vertexStartId = selectedVertexStartId
                val vertexEndId = selectedVertexEndId
                if (vertexStartId == null || vertexEndId == null) return@GraphRunnableOption
                val graphVertices = viewModel.vertices.value
                val vertexStart = graphVertices.find { it.id == vertexStartId }
                val vertexEnd = graphVertices.find { it.id == vertexEndId }
                if (vertexStart == null || vertexEnd == null) return@GraphRunnableOption
                val graphShortestPath = GraphDijkstraPathFinder()
                isRunning = true
                viewModel.runResetEdges()
                viewModel.runShortestPath(graphShortestPath, vertexStart, vertexEnd, { isRunning = false })
            },
            content = {
                NumberInputPicker(
                    text = "starting vertex id",
                    onChange = {
                        it?.let { selectedVertexStartId = it }
                    }
                )
                NumberInputPicker(
                    text = "ending vertex id",
                    onChange = {
                        it?.let { selectedVertexEndId = it }
                    }
                )
            }
        ),
        GraphRunnableOption(
            title = "Find MST",
            description = "Finds minimum spanning tree in the graph",
            onRun = {
                isRunning = true
                val graphMST = GraphMSTWithKruskal()
                viewModel.runResetEdges()
                viewModel.runMST(graphMST, { isRunning = false })
            }
        ),





    )

    Column(
        modifier = Modifier
            .then(modifier)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { isShown = !isShown },
                modifier = Modifier
            ) {
                Icon(Icons.Filled.Call, "settingsIcon")
            }
        }

        AnimatedVisibility(
            visible = isShown,
            enter = slideInHorizontally(initialOffsetX = { it }),
            exit = slideOutHorizontally(targetOffsetX = { it })
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp, 0.dp, 0.dp, 24.dp),
                shadowElevation = 8.dp,
                shape = MaterialTheme.shapes.large.copy(
                    topEnd = CornerSize(0),
                    bottomEnd = CornerSize(0)
                )
            ) {
                Column(
                    modifier = Modifier.padding(8.dp).verticalScroll(rememberScrollState())
                ) {
                    runnableOptions.forEach { option ->
                        ConfigurationOption(
                            modifier = Modifier.padding(4.dp),
                            title = option.title,
                            description = option.description,
                            onClick = option.onRun,
                            enabled = !isRunning,
                            content = option.content
                        )
                    }
                }
            }
        }
    }
}