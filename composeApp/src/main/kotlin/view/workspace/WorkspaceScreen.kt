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

fun setupCycle1(graph: Graph<Int, String>) {
    graph.addVertex(1)
    graph.addVertex(9)
    graph.addVertex(23)
    graph.addVertex(52)
    graph.addVertex(0)

    graph.addEdge(1, 23, "A")
    graph.addEdge(23, 52, "B")
    graph.addEdge(9, 23, "C")
    graph.addEdge(52, 0, "D")
    graph.addEdge(0, 23, "E")
    graph.addEdge(0, 9, "F")

    for (i in 100..1630) {
        graph.addVertex(i)
        val ri = (100..i).random()
        graph.addEdge(i, ri, "$i")
    }
}

class WorkspaceScreen : Screen {
    @Composable
    override fun Content() {
        val myGraph by remember { mutableStateOf(DirectedWeightedGraph<Int, String>()) }
        setupCycle1(myGraph)
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
                    GraphView(modifier = Modifier.fillMaxSize().clipToBounds(), GraphViewModel(myGraph, GraphPlacementYifanHu()))

                    // list workspace elements in a row
                    Row(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        // the tools to change behavior of the mouse
                        GraphToolBar(
                            modifier = Modifier.fillMaxHeight()
                        )

                        // empty space
                        Spacer(
                            modifier = Modifier.weight(1f)
                        )

                        // the sidebar to configure current graph
                        GraphConfiguration(
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