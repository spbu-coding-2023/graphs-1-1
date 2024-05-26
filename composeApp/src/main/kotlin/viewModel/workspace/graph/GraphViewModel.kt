package viewModel.workspace.graph

import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputChange
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
import view.workspace.graph.MAX_SCALE_FACTOR
import view.workspace.graph.MIN_SCALE_FACTOR
import viewModel.workspace.SelectAreaViewModel
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
        println("updated")
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
        val vs = graph.vertexSet()
        return if (vs.isEmpty()) 0 else vs.maxOf { it.id } + 1
    }

    fun createVertex(offset: Offset) {
        updateGraph { g ->
            val vertexPos = (offset - offsetFactor.value) / scaleFactor.value
            g.addVertex(VertexModel(getVertexNextId(), vertexPos.x, vertexPos.y, null))
        }
    }

    fun handleVertexOnDrag(change: PointerInputChange, dragAmount: Offset, vertex: VertexViewModel<V>) {
        when (interactionMode.value) {
            GraphInteractionMode.Drag -> {
                updateGraph {
                    println("updating vertex position from ${Offset(vertex.vertexState.value.x, vertex.vertexState.value.y)}")
                    val newOffset = Offset(vertex.vertexState.value.x, vertex.vertexState.value.y) + dragAmount / scaleFactor.value
                    vertex.setVertexOffset(newOffset)
                    println("to ${newOffset}")
//                    it.vertexSet().forEach { v ->
//                        if (v.vertexState.value.id == vertex.vertexState.value.id) {
//                            v.setVertexOffset(Offset(v.vertexState.value.x, v.vertexState.value.y) + dragAmount / scaleFactor.value)
//                        }
//                    }
                }
            }
            else -> return
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

    fun switchSelectionVertex(vertex: VertexModel<V>) {
        updateGraph { g ->
            vertex.isSelected = !vertex.isSelected
        }
    }

    fun vertexDrag(vertex: VertexModel<V>, dragAmount: Offset) {
        updateGraph { g ->
            vertex.x += dragAmount.x
            vertex.y += dragAmount.y
        }
        _updated.value = !_updated.value
    }

    fun removeAndAllIfSelected(vertex: VertexModel<V>) {
        updateGraph { g ->
            if (vertex.isSelected) {
                g.vertexSet().filter { v -> v.isSelected }.forEach { v ->
                    g.removeVertex(v)
                }
            } else g.removeVertex(vertex)
        }
    }

//    fun deleteVertex(vertexId: Int) {
//        updateGraph { g ->
//            g.vertexSet().first { v -> v.vertexState.value.id == vertexId }.let { v ->
//                println("removed ${v.vertexState.value}")
//                g.removeVertex(v)
//
//            }
//        }
//    }

//    fun handleVertexOnClick(offset: Offset, vertex: VertexViewModel<V>) {
//        when (interactionMode.value) {
//            GraphInteractionMode.Delete -> {
//                updateGraph {
//                    if (vertex.vertexState.value.isSelected) {
//                        updateGraph { g ->
//                            val selectedVertices = g.vertexSet().filter { v -> v.vertexState.value.isSelected }
//                            selectedVertices.forEach { v -> g.removeVertex(v) }
//                        }
//                    } else {
//                        it.removeVertex(vertex)
//                    }
//                }
//            }
//            GraphInteractionMode.Select -> {
//                vertex.setVertexSelect(!vertex.vertexState.value.isSelected)
//            }
//            else -> return
//        }
//    }

    fun handleGraphOnDrag(change: PointerInputChange, dragAmount: Offset, selectAreaViewModel: SelectAreaViewModel) {
        when(interactionMode.value) {
            GraphInteractionMode.Pan -> {
                setOffsetFactor(offsetFactor.value + dragAmount)
            }
            GraphInteractionMode.Select -> { // select all vertices in box
                selectAreaViewModel.incAreaSize(dragAmount)
            }
            else -> return
        }
    }

    fun handlePan(dragAmount: Offset) {
        setOffsetFactor(offsetFactor.value + dragAmount)
    }

    fun handleGraphOnDragStart(offset: Offset, selectAreaViewModel: SelectAreaViewModel) {
        when(interactionMode.value) {
            GraphInteractionMode.Select -> {
                selectAreaViewModel.setStartOffset(offset)
            }
            else -> return
        }
    }

//    fun handleGraphOnDragEnd(selectAreaViewModel: SelectAreaViewModel) {
//        selectAreaViewModel.selectAreaState.value.startOffset
//        when(interactionMode.value) {
//            GraphInteractionMode.Select -> {
//                updateGraph { g ->
//                    val selectedVertices = g.vertexSet().filter { v ->
//                        val vx = v.vertexState.value.x*scaleFactor.value + offsetFactor.value.x
//                        val vy = v.vertexState.value.y*scaleFactor.value + offsetFactor.value.y
//                        val ax = selectAreaViewModel.selectAreaState.value.startOffset.x
//                        val ay = selectAreaViewModel.selectAreaState.value.startOffset.y
//                        val sx = selectAreaViewModel.selectAreaState.value.areaSize.x
//                        val sy = selectAreaViewModel.selectAreaState.value.areaSize.y
//                        (((vx >= ax) && (vx <= ax + sx)) || ((vx <= ax) && (vx >= ax + sx))) &&
//                        (((vy >= ay) && (vy <= ay + sy)) || ((vy <= ay) && (vy >= ay + sy)))
//                    }
//
//                    selectedVertices.forEach { v ->
//                        v.setVertexSelect(true)
//                    }
//                }
//
//                selectAreaViewModel.setAreaSize(Offset.Zero)
//            }
//            else -> return
//        }
//    }

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

//    fun handleGraphOnClick(offset: Offset) {
//        when(interactionMode.value) {
//            GraphInteractionMode.Create -> {
//                updateGraph {
//                    it.addVertex(VertexViewModel(
//                        VertexModel(
//                            getVertexNextId(),
//                            (offset.x - offsetFactor.value.x)/scaleFactor.value,
//                            (offset.y - offsetFactor.value.y)/scaleFactor.value,
//                            null
//                    )))
//                }
//            }
//            GraphInteractionMode.Select -> {
//                updateGraph {
//                    it.vertexSet().forEach { v -> v.setVertexSelect(false) }
//                }
//            }
//            else -> return
//        }
//    }
}
