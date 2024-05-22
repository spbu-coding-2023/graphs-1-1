package view.workspace.graph

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import graphses.composeapp.generated.resources.Res
import graphses.composeapp.generated.resources.cat
import model.VertexModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import viewModel.workspace.graph.GraphInteractionMode
import viewModel.workspace.graph.GraphViewModel
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
    val vertices by viewModel.vertices.collectAsState()
    val edges by viewModel.edges.collectAsState()
    val interactionMode by viewModel.interactionMode.collectAsState()
    val scaleFactor by viewModel.scaleFactor.collectAsState()
    val offsetFactor by viewModel.offsetFactor.collectAsState()
    var reRender by remember { mutableStateOf(false) }

    var selectBoxStartOffset by remember { mutableStateOf(Offset.Zero) }
    var selectBoxSize by remember { mutableStateOf(Offset.Zero) }
    var selectBoxShow by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .onPointerEvent(PointerEventType.Scroll) {
                if (interactionMode != GraphInteractionMode.Pan) return@onPointerEvent

                val point = it.changes.first()
                if (point.scrollDelta.y == 0f) return@onPointerEvent

                val delta = (scaleFactor - MIN_SCALE_FACTOR)/(MAX_SCALE_FACTOR - MIN_SCALE_FACTOR)/10 + 1.05f // max(1f, min(1.08f, exp(scaleFactor/100))) // zoom factor

                val deltaZoom = if (point.scrollDelta.y < 0) delta else 1/delta
                val scaleChange = scaleFactor * deltaZoom

                if (scaleChange < MIN_SCALE_FACTOR || scaleChange > MAX_SCALE_FACTOR) return@onPointerEvent // zoom range

                val offsetChange = point.position * (1f - deltaZoom) + offsetFactor * deltaZoom

                viewModel.setScaleFactor(scaleChange)
                viewModel.setOffsetFactor(offsetChange)
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        when(interactionMode) {
                            GraphInteractionMode.Select -> {
                                selectBoxShow = true
                                selectBoxStartOffset = it
                            }
                            else -> return@detectDragGestures
                        }
                    },
                    onDrag = { change, dragAmount ->
                        when(interactionMode) {
                            GraphInteractionMode.Pan -> {
                                viewModel.setOffsetFactor(offsetFactor + dragAmount)
                            }
                            GraphInteractionMode.Select -> { // select all vertices in box
                                selectBoxSize += dragAmount
                            }
                            else -> return@detectDragGestures
                        }
                    },
                    onDragEnd = {
                        when(interactionMode) {
                            GraphInteractionMode.Select -> {
                                selectBoxShow = false

                                viewModel.updateGraph { g ->
                                    val selectedVertices = g.vertexSet().filter { v ->
                                        val vx = v.x*scaleFactor + offsetFactor.x
                                        val vy = v.y*scaleFactor + offsetFactor.y
                                        (
                                        ((vx >= selectBoxStartOffset.x) && (vx <= selectBoxStartOffset.x + selectBoxSize.x)) ||
                                        ((vx <= selectBoxStartOffset.x) && (vx >= selectBoxStartOffset.x + selectBoxSize.x))
                                        ) &&
                                        (
                                        ((vy >= selectBoxStartOffset.y) && (vy <= selectBoxStartOffset.y + selectBoxSize.y)) ||
                                        ((vy <= selectBoxStartOffset.y) && (vy >= selectBoxStartOffset.y + selectBoxSize.y))
                                        )
                                    }

                                    selectedVertices.forEach { v ->
                                        v.isSelected = true
                                    }
                                }

                                selectBoxSize = Offset.Zero
                                reRender = !reRender
                            }
                            else -> return@detectDragGestures
                        }
                    }


                )
            }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    when(interactionMode) {
                        GraphInteractionMode.Create -> {
                            viewModel.updateGraph {
                                it.addVertex(VertexModel(viewModel.getVertexNextId(), (offset.x - offsetFactor.x)/scaleFactor, (offset.y - offsetFactor.y)/scaleFactor, null))
                            }
                        }
                        GraphInteractionMode.Select -> {
                            viewModel.updateGraph {
                                it.vertexSet().forEach { v -> v.isSelected = false }
                                reRender = !reRender
                            }
                        }
                        else -> return@detectTapGestures
                    }
                    if (interactionMode != GraphInteractionMode.Create) return@detectTapGestures

                }
            }
    ) {
        val EDGE_COLOR = MaterialTheme.colorScheme.outline

        // re-renders on keys change
        key(scaleFactor, offsetFactor, viewModel, interactionMode, reRender) {
            // Draw edges on a single canvas
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
//                        if (viewModel.isEdgesDirected()) {
//                            drawRect(
//                                color = EDGE_COLOR.copy(alpha = EDGE_ALPHA),
//                                topLeft = Offset(headVertex.x * scaleFactor + offsetFactor.x, headVertex.y * scaleFactor + offsetFactor.y),
//                                size = Size(EDGE_STROKE_WIDTH*scaleFactor*14, EDGE_STROKE_WIDTH*scaleFactor*14)
//                            )
//                        }
                    }
                }
            }
            // Overlay vertices on top of the canvas
            vertices.forEach { vertex ->
                DraggableVertex(vertex, scaleFactor, interactionMode, viewModel)
            }

            key(selectBoxShow) {
                val selectBoxColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f)
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // select rect
                    drawRect(
                        color = selectBoxColor,
                        topLeft = selectBoxStartOffset,
                        size = Size(selectBoxSize.x, selectBoxSize.y)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun <V, E>DraggableVertex(vertex: VertexModel<V>, scaleFactor: Float, interactionMode: GraphInteractionMode, viewModel: GraphViewModel<V, E>) {
    val offsetFactor by viewModel.offsetFactor.collectAsState()
    var offsetX by remember { mutableStateOf(vertex.x * scaleFactor + offsetFactor.x) }
    var offsetY by remember { mutableStateOf(vertex.y * scaleFactor + offsetFactor.y) }
    val vertexSize by remember { mutableStateOf(VERTEX_SIZE*scaleFactor) }
    var reRender by remember { mutableStateOf(false) }
    val vertexSelectColor by animateColorAsState(
        if (vertex.isSelected) MaterialTheme.colorScheme.surfaceBright else MaterialTheme.colorScheme.surfaceDim
    )

    key (reRender) {
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .size(vertexSize.dp)
                .graphicsLayer {
                    translationX = -vertexSize
                    translationY = -vertexSize
                }
                .background(vertexSelectColor.copy(alpha = VERTEX_ALPHA), shape = CircleShape)
                .border(
                    width = (vertexSize/16).dp,
                    brush = SolidColor(MaterialTheme.colorScheme.outline),
                    shape = CircleShape
                )
                .pointerInput(Unit) {
                    if (interactionMode != GraphInteractionMode.Drag) return@pointerInput
                    detectDragGestures { change, dragAmount ->
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                        viewModel.updateGraph {
                            it.vertexSet().forEach { v ->
                                if (v.id == vertex.id) {
                                    v.x = (offsetX - offsetFactor.x) / scaleFactor
                                    v.y = (offsetY - offsetFactor.y) / scaleFactor
                                }
                            }
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures {
                        when (interactionMode) {
                            GraphInteractionMode.Delete -> {
                                viewModel.updateGraph {
                                    if (vertex.isSelected) {
                                        viewModel.updateGraph { g ->
                                            val selectedVertices = g.vertexSet().filter { v -> v.isSelected }
                                            selectedVertices.forEach { v -> g.removeVertex(v) }
                                        }
                                    } else {
                                        it.removeVertex(vertex)
                                    }
                                }
                            }

                            GraphInteractionMode.Select -> {
                                vertex.isSelected = !vertex.isSelected
                                reRender = !reRender
                            }

                            else -> return@detectTapGestures
                        }
                    }
                }
        )
    }
}
