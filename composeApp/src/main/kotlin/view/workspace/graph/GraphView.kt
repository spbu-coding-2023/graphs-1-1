package view.workspace.graph

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.lifecycle.viewModelScope
import graphses.composeapp.generated.resources.Res
import graphses.composeapp.generated.resources.cat
import model.EdgeModel
import model.VertexModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import viewModel.workspace.SelectAreaViewModel
import viewModel.workspace.graph.EdgeViewModel
import viewModel.workspace.graph.GraphInteractionMode
import viewModel.workspace.graph.GraphViewModel
import viewModel.workspace.graph.VertexViewModel
import kotlin.math.*

val MAX_SCALE_FACTOR = 25f
val MIN_SCALE_FACTOR = .1f

val EDGE_STROKE_WIDTH = 2f
val EDGE_ALPHA = 0.5f

val VERTEX_SIZE = 23f
val VERTEX_ALPHA = 1f

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <V, E>GraphView(viewModel: GraphViewModel<V, E>) {
    val selectionArea by viewModel.selectionArea.collectAsState()
    val vertices by viewModel.vertices.collectAsState()
    val edges by viewModel.edges.collectAsState()
    val scaleFactor by viewModel.scaleFactor.collectAsState()
    val offsetFactor by viewModel.offsetFactor.collectAsState()
    val interactionMode by viewModel.interactionMode.collectAsState()
    val updated by viewModel.updated.collectAsState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .onPointerEvent(PointerEventType.Scroll) {pointerEvent ->
                when (interactionMode) {
                    GraphInteractionMode.Pan -> {
                        viewModel.handleGraphScroll(pointerEvent)
                    }
                    GraphInteractionMode.Select -> {
                        viewModel.handleGraphScroll(pointerEvent)
                    }
                    GraphInteractionMode.Delete -> {
                        viewModel.handleGraphScroll(pointerEvent)
                    }
                    GraphInteractionMode.Drag -> {
                        viewModel.handleGraphScroll(pointerEvent)
                    }
                    GraphInteractionMode.Create -> {
                        viewModel.handleGraphScroll(pointerEvent)
                    }
                    else -> return@onPointerEvent
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { startOffset ->
                        when (interactionMode) {
                            GraphInteractionMode.Select -> {
                                viewModel.startSelectionArea(startOffset)
                            }
                            else -> return@detectDragGestures
                        }
                    },
                    onDrag = { change, dragAmount ->
                        when (interactionMode) {
                            GraphInteractionMode.Pan -> {
                                viewModel.handlePan(dragAmount)
                            }
                            GraphInteractionMode.Drag -> {
                                vertices.find { it.isSelected }?.let {
                                    viewModel.handleDragVertex(it.id, dragAmount)
                                }
                            }
                            GraphInteractionMode.Select -> {
                                viewModel.updateSelectionArea(change.position)
                            }
                            else -> return@detectDragGestures
                        }
                    },
                    onDragEnd = {
                        when (interactionMode) {
                            GraphInteractionMode.Select -> {
                                viewModel.endSelectionArea()
                            }
                            else -> return@detectDragGestures
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    when (interactionMode) {
                        GraphInteractionMode.Select -> {
                            viewModel.deselectAll()
                        }
                        GraphInteractionMode.Create -> {
                            viewModel.createVertex(offset)
                        }
                        else -> return@detectTapGestures
                    }
                }
            }
    ) {

        EdgesView(vertices, edges, scaleFactor, offsetFactor)

        key(updated) {
            vertices.forEach { v ->
                DraggableVertex(
                    v,
                    scaleFactor,
                    offsetFactor,
                    onDrag = { change, dragAmount ->
                        when (interactionMode) {
                            GraphInteractionMode.Pan -> {
                                viewModel.handlePan(dragAmount)
                            }
                            GraphInteractionMode.Drag -> {
                                println("raggin $dragAmount")
                                viewModel.vertexDrag(v, dragAmount / scaleFactor)
                            }
                            else -> return@DraggableVertex
                        }
                    },
                    onClick = { offset ->
                        when (interactionMode) {
                            GraphInteractionMode.Select -> {
                                viewModel.switchSelectionVertex(v)
                            }
                            GraphInteractionMode.Delete -> {
                                viewModel.removeAndAllIfSelected(v)
                            }
                            else -> return@DraggableVertex
                        }
                    }
                )

            }
        }

        selectionArea?.let { area ->
            DrawSelectArea(area)
        }

    }
}

@Composable
fun DrawSelectArea(area: Rect) {
    val selectAreaColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f)

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            color = selectAreaColor,
            topLeft = area.topLeft,
            size = Size(area.width, area.height)
        )
    }

}

@Composable
fun <V>DraggableVertex(vertex: VertexModel<V>, scaleFactor: Float, offsetFactor: Offset, onDrag: (PointerInputChange, Offset) -> Unit, onClick: (Offset) -> Unit) {
    val offsetX by rememberUpdatedState(vertex.x * scaleFactor + offsetFactor.x)
    val offsetY by rememberUpdatedState(vertex.y * scaleFactor + offsetFactor.y)
    val vertexSize by rememberUpdatedState(VERTEX_SIZE*scaleFactor)
    var reRender by remember { mutableStateOf(false) }

    key (reRender) {
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .size(vertexSize.dp)
                .graphicsLayer {
                    translationX = -vertexSize
                    translationY = -vertexSize
                }
                .background(
            color = (if (vertex.isSelected) MaterialTheme.colorScheme.surfaceBright else MaterialTheme.colorScheme.surfaceDim).copy(alpha = VERTEX_ALPHA),
            shape = CircleShape
                )
                .border(
                    width = (vertexSize/16).dp,
                    brush = SolidColor(MaterialTheme.colorScheme.outline),
                    shape = CircleShape
                )
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            onDrag(change, dragAmount)
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        onClick(offset)
                        reRender = !reRender
                    }
                }
        ) {
            Text("${vertex.id}")
        }
    }

}

@Composable
fun <V, E>EdgesView(vertices: List<VertexModel<V>>, edges: List<EdgeModel<E>>, scaleFactor: Float, offsetFactor: Offset) {
    val EDGE_COLOR = MaterialTheme.colorScheme.outline

    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {
        edges.forEach { edge ->
            val tailVertex = vertices.find { it.id == edge.tailVertexId }
            val headVertex = vertices.find { it.id == edge.headVertexId }
            if (tailVertex != null && headVertex != null) {
                drawLine(
                    color = EDGE_COLOR.copy(alpha = EDGE_ALPHA),
                    start = Offset(tailVertex.x * scaleFactor + offsetFactor.x, tailVertex.y * scaleFactor + offsetFactor.y),
                    end = Offset(headVertex.x * scaleFactor + offsetFactor.x, headVertex.y * scaleFactor + offsetFactor.y),
                    strokeWidth = EDGE_STROKE_WIDTH*scaleFactor
                )
            }
        }
    }
}
