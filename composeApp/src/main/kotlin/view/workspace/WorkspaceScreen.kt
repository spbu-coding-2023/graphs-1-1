package view.workspace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import display.placement.implementation.GraphPlacementYifanHu
import graph.Graph
import graph.implementation.DirectedWeightedGraph
import view.workspace.graphConfiguration.GraphConfiguration
import view.workspace.graphToolBar.GraphToolBar
import view.workspace.graph.GraphView
import view.workspace.header.Header
import viewModel.workspace.graph.GraphViewModel

class WorkspaceScreen<V, E>(val viewModel: GraphViewModel<V, E>) : Screen {
    @Composable
    override fun Content() {
        Scaffold(
            topBar = {
                // the header of the screen
                Header("(graph name)")
            },
            content = {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                ) {
                    // draw graph
                    GraphView(
                        viewModel = viewModel
                    )

                    // list workspace elements in a row
                    Row(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        // the tools to change behavior of the mouse
                        GraphToolBar(
                            viewModel = viewModel,
                            modifier = Modifier.fillMaxHeight()
                        )

                        // empty space
                        Spacer(
                            modifier = Modifier.weight(1f)
                        )

                        // the sidebar to configure current graph
                        GraphConfiguration(
                            viewModel = viewModel,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(340.dp)
                        )
                    }
                }
            }
        )
    }
}
