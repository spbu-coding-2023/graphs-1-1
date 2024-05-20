package viewModel.workspace.graph

import androidx.lifecycle.ViewModel
import display.placement.GraphPlacement
import graph.Graph

class GraphEdgesViewModel(
    val scale: Float,
    val edgePlacement: Set<Pair<Pair<Float, Float>, Pair<Float, Float>>>,
    val isDirected: Boolean
) : ViewModel() {

}