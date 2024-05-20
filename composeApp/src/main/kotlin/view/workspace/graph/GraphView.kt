package view.workspace.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import graph.implementation.DirectedWeightedGraph
import view.workspace.graph.graphEdges.GraphEdgesView
import view.workspace.graph.graphVertecies.GraphVerteciesView
import viewModel.workspace.graph.GraphEdgesViewModel

@Composable
fun GraphView(modifier: Modifier) {
    val myGraph = DirectedWeightedGraph<Int, String>()
    myGraph.addVertex(23)
    myGraph.addVertex(52)
    myGraph.addEdge(23, 52, "A")

    GraphEdgesView(GraphEdgesViewModel(myGraph))
    GraphVerteciesView()
}