package viewModel.workspace.graph

import androidx.compose.runtime.key
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.lifecycle.ViewModel
import display.community.GraphCommunity
import display.keyVertex.GraphKeyVertex
import display.minimumSpanningTree.GraphMST
import display.pathSearch.GraphPathSearch
import display.placement.GraphPlacement
import display.stronglyConnectedComponentSearch.implementation.GraphSCCSearchWithTarjan
import graph.Graph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.EdgeModel
import model.VertexModel
import view.workspace.graph.MAX_SCALE_FACTOR
import view.workspace.graph.MIN_SCALE_FACTOR
import viewModel.workspace.graph.utils.GraphStorage
import java.lang.Math.pow
import kotlin.math.max
import kotlin.math.min

enum class GraphInteractionMode {Pan, Delete, Drag, Create, Select}

class GraphViewModel(
    initialGraph: Graph<VertexModel, EdgeModel>,
    val graphName: String
) : ViewModel() {
    private val graph = initialGraph
    val storage = GraphStorage({ graph }, graphName)

    private val _vertices = MutableStateFlow<List<VertexModel>>(listOf())
    val vertices: StateFlow<List<VertexModel>> = _vertices

    private val _edges = MutableStateFlow<List<EdgeModel>>(listOf())
    val edges: StateFlow<List<EdgeModel>> = _edges

    private val _scaleFactor = MutableStateFlow(1f)
    val scaleFactor: StateFlow<Float> = _scaleFactor

    private val _offsetFactor = MutableStateFlow(Offset.Zero)
    val offsetFactor: StateFlow<Offset> = _offsetFactor

    private val _interactionMode = MutableStateFlow(GraphInteractionMode.Pan)
    val interactionMode: StateFlow<GraphInteractionMode> = _interactionMode

    private val _selectionArea = MutableStateFlow<Rect?>(null)
    val selectionArea: StateFlow<Rect?> = _selectionArea

    private val _updated = MutableStateFlow(false)
    val updated: StateFlow<Boolean> = _updated

    init {
        updateState()
    }

    fun setInteractionMode(mode: GraphInteractionMode) {
        _interactionMode.value = mode
    }

    fun updateState() {
        _vertices.value = graph.vertexSet().toList()
        _edges.value = graph.edgeSet().toList()
        _updated.value = !_updated.value
    }

    fun updateGraph(actionOnGraph: (Graph<VertexModel, EdgeModel>) -> Unit) {
        actionOnGraph(graph)
        updateState()
    }

    fun setScaleFactor(factor: Float) {
        _scaleFactor.value = factor
    }

    fun setOffsetFactor(factor: Offset) {
        _offsetFactor.value = factor
    }

    fun startSelectionArea(offset: Offset) {
        _selectionArea.value = Rect(offset, Size.Zero)
    }

    fun updateSelectionArea(offset: Offset) {
        selectionArea.value?.let {
            _selectionArea.value = Rect(it.topLeft, offset)
        }
    }

    fun endSelectionArea() {
        _selectionArea.value?.let { area ->
            val area = Rect(
                if (area.width >= 0) area.left else area.left + area.width,
                if (area.height >= 0) area.top else area.top + area.height,
                if (area.width >= 0) area.right else area.left,
                if (area.height >= 0) area.bottom else area.top
            )
            updateGraph { g ->
                g.vertexSet().forEach { v ->
                    val isSelected = area.contains(Offset(v.x * scaleFactor.value + offsetFactor.value.x, v.y * scaleFactor.value + offsetFactor.value.y))
                    if (isSelected) v.isSelected = true
                }
            }
        }
        _selectionArea.value = null
    }

    fun deselectAll() {
        updateGraph { g ->
            g.vertexSet().forEach { v -> v.isSelected = false }
        }
    }

    fun runPlacement(graphPlacement: GraphPlacement, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            updateGraph { g ->
                val placement = graphPlacement.getPlacement(g)
                g.vertexSet().forEach {
                    if (placement[it] != null) {
                        val x = placement[it]!!.first
                        val y = placement[it]!!.second
                        it.x = x
                        it.y = y
                    }
                }
            }

            withContext(Dispatchers.Main) {
                onFinished()
            }
        }
    }

    fun getVertexNextId(): Int {
//        var nextId = 0
//        val verticesId = graph.vertexSet().map { it.id }
//        while (verticesId.contains(nextId)) nextId++ // TODO: awfully slow, (keep deleted vertices in a queue, or store them in heap ds)
//        return nextId
        val vs = graph.vertexSet()
        return if (vs.isEmpty()) 0 else vs.maxOf { it.id } + 1
    }

    fun createVertex(offset: Offset) {
        updateGraph { g ->
            val vertexPos = (offset - offsetFactor.value) / scaleFactor.value
            g.addVertex(VertexModel(getVertexNextId(), vertexPos.x, vertexPos.y, null))
        }
    }

    fun handleDragVertex(vertexId: Int, dragAmount: Offset) {
        updateGraph { g ->
            g.vertexSet().first { v -> v.id == vertexId }.let {
                it.x += dragAmount.x
                it.y += dragAmount.y
            }
        }
    }

    fun switchSelectionVertex(vertex: VertexModel) {
        updateGraph { g ->
            vertex.isSelected = !vertex.isSelected
        }
    }

    fun removeAndAllIfSelected(vertex: VertexModel) {
        updateGraph { g ->
            if (vertex.isSelected) {
                g.vertexSet().filter { v -> v.isSelected }.forEach { v ->
                    g.removeVertex(v)
                }
            } else g.removeVertex(vertex)
        }
    }

    fun handlePan(dragAmount: Offset) {
        setOffsetFactor(offsetFactor.value + dragAmount)
    }

    fun handleGraphScroll(pointerEvent: PointerEvent) {
        val point = pointerEvent.changes.first()
        if (point.scrollDelta.y == 0f) return

        val delta = (scaleFactor.value - MIN_SCALE_FACTOR)/(MAX_SCALE_FACTOR - MIN_SCALE_FACTOR)/10 + 1.05f // max(1f, min(1.08f, exp(scaleFactor/100))) // zoom factor

        val deltaZoom = if (point.scrollDelta.y < 0) delta else 1/delta
        val scaleChange = scaleFactor.value * deltaZoom

        if (scaleChange < MIN_SCALE_FACTOR || scaleChange > MAX_SCALE_FACTOR) return // zoom range

        val offsetChange = point.position * (1f - deltaZoom) + offsetFactor.value * deltaZoom

        setScaleFactor(scaleChange)
        setOffsetFactor(offsetChange)
    }

    fun runKeyVertex(keyVertex: GraphKeyVertex, maxIncreaseSize: Float = 1f, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            updateGraph { g ->
                val keyVertices = keyVertex.getKeyVertices(g)
                val maxKeyVertex = keyVertices.values.max()
                g.vertexSet().forEach { v ->
                    keyVertices[v]?.let {
                        v.size = max(1f, it / maxKeyVertex * maxIncreaseSize)
                    }
                }
            }

            withContext(Dispatchers.Main) {
                onFinished()
            }
        }
    }

    fun runResetEdges() {
        updateGraph { g ->
            g.edgeSet().forEach { e -> e.isHighlighted = false }
        }
    }

    fun runShortestPath(graphPathSearch: GraphPathSearch, vertexFrom: VertexModel, vertexTo: VertexModel, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            updateGraph { g ->
                val shortestPath = graphPathSearch.searchPath(g, vertexFrom, vertexTo)
                repeat(shortestPath.size-1) { i ->
                    g.getEdge(shortestPath[i], shortestPath[i+1])?.isHighlighted = true
                }
            }

            withContext(Dispatchers.Main) {
                onFinished()
            }
        }
    }

    fun runMST(graphMST: GraphMST, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            updateGraph { g ->
                graphMST.getMST(g).forEach { it.isHighlighted = true }
            }

            withContext(Dispatchers.Main) {
                onFinished()
            }
        }
    }

    fun runResetCommunities() {
        updateGraph { g ->
            g.vertexSet().forEach { v -> v.communityId = 0 }
        }
    }

    fun runCommunity(graphCommunity: GraphCommunity, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            updateGraph { g ->
                val verticesCommunity = graphCommunity.getCommunities(g)
                g.vertexSet().forEach { v ->
                    val vertexCommunity = verticesCommunity[v]
                    if (vertexCommunity == null) v.communityId = 0
                    else v.communityId = vertexCommunity
                }
            }

            withContext(Dispatchers.Main) {
                onFinished()
            }
        }
    }

    fun runSGG(graphSCC: GraphSCCSearchWithTarjan<VertexModel, EdgeModel>, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            updateGraph { g ->
                val listOfCommunities = graphSCC.getSCCs(g)
                listOfCommunities.forEachIndexed { i, communityOfVertices ->
                    communityOfVertices.forEach { v -> v.communityId = i }
                }
            }

            withContext(Dispatchers.Main) {
                onFinished()
            }
        }
    }

    fun createEdge(vertexFrom: VertexModel, vertexTo: VertexModel) {
        updateGraph { g ->
            g.addEdge(vertexFrom, vertexTo, EdgeModel(vertexFrom.id, vertexTo.id, null, isDirected = isDirected()))
        }
    }

    fun setupRandom(n: Int) {
        val vv = mutableListOf<VertexModel>()
        val r = 1000

        for (i in 0..n) {
            vv.add(VertexModel(i, (0..r).random().toFloat(), (0..r).random().toFloat(), i.toString()))
            graph.addVertex(vv.last())
            repeat(2) {
                val ri = (pow((0..i).random().toDouble(), 2.952)/pow(i.toDouble(), 2.952)*i).toInt()
                val randW = (-1..3).random()
                graph.addEdge(vv[i], vv[ri], EdgeModel(i, ri, "$i", weight = randW.toDouble()), randW.toDouble())
            }
        }
    }

    fun isWeighted(): Boolean {
        return graph.configuration.isWeighted()
    }

    fun isEdgesPositive(): Boolean {
        return !isWeighted() || edges.value.all { it.weight >= 0 }
    }

    fun isDirected(): Boolean {
        return graph.configuration.isDirected()
    }

}
