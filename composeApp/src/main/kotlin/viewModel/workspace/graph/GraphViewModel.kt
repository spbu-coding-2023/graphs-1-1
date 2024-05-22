package viewModel.workspace.graph

import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import display.placement.GraphPlacement
import display.placement.implementation.GraphPlacementRandom
import display.placement.implementation.GraphPlacementYifanHu
import graph.Graph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.EdgeModel
import model.VertexModel
import kotlin.concurrent.thread

enum class GraphInteractionMode {Pan, Delete, Drag, Create, Select}

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

    private val _interactionMode = MutableStateFlow(GraphInteractionMode.Pan)
    val interactionMode: StateFlow<GraphInteractionMode> = _interactionMode

    init {
//        val graphPlacement = GraphPlacementYifanHu()
//        graphPlacement.setSize(4, 4)
//        runPlacement(graphPlacement, {})
        updateState()
    }

    fun setInteractionMode(mode: GraphInteractionMode) {
        _interactionMode.value = mode
    }

    fun updateState() {
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

    fun runPlacement(graphPlacement: GraphPlacement, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            val placement = graphPlacement.getPlacement(graph)
            graph.vertexSet().forEach {
                val x = placement[it]!!.first
                val y = placement[it]!!.second
                it.x = x
                it.y = y
            }
            updateState()
            withContext(Dispatchers.Main) {
                onFinished()
            }
        }
    }
    /**
     * Returns if edge is directed or not
     */
    fun isEdgesDirected(): Boolean {
        return graph.configuration.isDirected()
    }

    fun getVertexNextId(): Int {
//        var nextId = 0
//        val verticesId = graph.vertexSet().map { it.id }
//        while (verticesId.contains(nextId)) nextId++ // TODO: awfully slow, (keep deleted vertices in a queue, or store them in heap ds)
//        return nextId
        return graph.vertexSet().maxOf { it.id } + 1
    }
}
