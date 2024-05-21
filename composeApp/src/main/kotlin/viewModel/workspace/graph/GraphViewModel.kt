package viewModel.workspace.graph

import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import display.placement.GraphPlacement
import display.placement.implementation.GraphPlacementYifanHu
import graph.Graph
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import model.EdgeModel
import model.VertexModel

class GraphViewModel<V, E>(
    initialGraph: Graph<VertexModel<V>, EdgeModel<E>>
) : ViewModel() {
    private val graph = initialGraph

    private val _vertices = MutableStateFlow<List<VertexModel<V>>>(listOf())
    val vertices: StateFlow<List<VertexModel<V>>> = _vertices

    private val _edges = MutableStateFlow<List<EdgeModel<E>>>(listOf())
    val edges: StateFlow<List<EdgeModel<E>>> = _edges

    private val _scaleFactor = MutableStateFlow(1f)
    val scaleFactor: StateFlow<Float> = _scaleFactor

    private val _offsetFactor = MutableStateFlow(Offset.Zero)
    val offsetFactor: StateFlow<Offset> = _offsetFactor

    private val _rotationFactor = MutableStateFlow(0f)
    val rotationFactor: StateFlow<Float> = _rotationFactor

    init {
//         runPlacement(GraphPlacementYifanHu())
        updateState()
    }

    private fun updateState() {
        _vertices.value = graph.vertexSet().toList()
        _edges.value = graph.edgeSet().toList()
    }

    fun updateGraph(actionOnGraph: (Graph<VertexModel<V>, EdgeModel<E>>) -> Unit) {
        actionOnGraph(graph)
        updateState()
    }

    fun setScaleFactor(factor: Float) {
        _scaleFactor.value = factor
    }
    fun setRotationFactor(factor: Float) {
        _rotationFactor.value = factor
    }
    fun setOffsetFactor(factor: Offset) {
        _offsetFactor.value = factor
    }

    fun runPlacement(graphPlacement: GraphPlacement) {
        val placement = graphPlacement.getPlacement(graph)
        graph.vertexSet().forEach {
            it.copy(
                id = it.id,
                x = placement[it]!!.first,
                y = placement[it]!!.second,
                data = it.data
            )
        }
        updateState()
    }
    /**
     * Returns if edge is directed or not
     */
    fun isEdgesDirected(): Boolean {
        return graph.configuration.isDirected()
    }
}
