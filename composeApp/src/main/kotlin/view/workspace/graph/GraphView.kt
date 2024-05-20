package view.workspace.graph

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import display.placement.implementation.GraphPlacementYifanHu
import view.workspace.graph.graphEdges.GraphEdgesView
import view.workspace.graph.graphVertecies.GraphVerteciesView
import viewModel.workspace.graph.GraphEdgesViewModel
import viewModel.workspace.graph.GraphVertexViewModel
import viewModel.workspace.graph.GraphViewModel

@Composable
fun <V, E>GraphView(modifier: Modifier, graphViewModel: GraphViewModel<V, E>) {
    val scale = 1f
    Box(
        modifier = Modifier.then(modifier)
    ) {
        GraphEdgesView(
            GraphEdgesViewModel(
            scale = scale,
            edgePlacement = graphViewModel.getEdgesPlacement(),
            isDirected = graphViewModel.isEdgesDirected()
        )
        )
        GraphVerteciesView(
            GraphVertexViewModel(
                scale = scale,
                radiusScale = 10f,
                verteciesPlacement = graphViewModel.getVerteciesPlacement()
            )
        )
    }
}
