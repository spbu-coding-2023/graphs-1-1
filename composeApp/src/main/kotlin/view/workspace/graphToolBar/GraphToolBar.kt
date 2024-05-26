package view.workspace.graphToolBar

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import viewModel.workspace.graph.GraphInteractionMode
import viewModel.workspace.graph.GraphViewModel

data class GraphTool(val icon: ImageVector, val name: String, val interactiveMode: GraphInteractionMode) {
    fun <V, E>onClick(viewModel: GraphViewModel<V, E>) {
        viewModel.setInteractionMode(interactiveMode)
    }
}

@Composable
fun <V, E>GraphToolBar(viewModel: GraphViewModel<V, E>, modifier: Modifier) {
    val interactionMode = viewModel.interactionMode.collectAsState()
    val tools = listOf(
        GraphTool(Icons.Filled.PanTool, "Pan", GraphInteractionMode.Pan),
        GraphTool(Icons.Filled.Delete, "Delete", GraphInteractionMode.Delete),
        GraphTool(Icons.Filled.OpenWith, "Drag", GraphInteractionMode.Drag),
        GraphTool(Icons.Filled.AddCircleOutline, "Create", GraphInteractionMode.Create),
        GraphTool(Icons.Filled.SelectAll, "Select", GraphInteractionMode.Select),
    )
    Column(
        modifier = Modifier
            .then(modifier),
        verticalArrangement = Arrangement.Center
    ) {
        tools.forEach { tool ->
            Row {
                Box(
                    modifier = Modifier
                        .animateContentSize()
                        .width(if (interactionMode.value == tool.interactiveMode) 12.dp else 0.dp)
                )
                SmallFloatingActionButton(
                    onClick = { tool.onClick(viewModel) },
                    modifier = Modifier
                        .padding(8.dp, 8.dp, 8.dp, 8.dp),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.secondary,
                ) {
                    Icon(tool.icon, tool.name)
                }
            }
        }
    }
}