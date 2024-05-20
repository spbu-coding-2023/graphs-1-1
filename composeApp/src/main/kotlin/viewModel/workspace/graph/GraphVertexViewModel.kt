package viewModel.workspace.graph

import androidx.lifecycle.ViewModel
import graph.Graph

class GraphVertexViewModel(
    val verteciesPlacement: Set<Pair<Float, Float>>,
    val scale: Float,
    val radiusScale: Float
) : ViewModel() {

}