package view.workspace.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
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
                    onDrag = { change, dragAmount ->
                        if (interactionMode != GraphInteractionMode.Pan) return@detectDragGestures
                        viewModel.setOffsetFactor(offsetFactor + dragAmount)
                    }
                )
            }
    ) {
        val EDGE_COLOR = MaterialTheme.colors.onBackground
        // runs ones at first render of the component
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()
        LaunchedEffect(Unit) {
            viewModel.setOffsetFactor(Offset(width/3, height/2))
        }

        // re-renders on keys change
        key(scaleFactor, offsetFactor, vertices, interactionMode) {
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
                    }
                }
            }
            // Overlay vertices on top of the canvas
            vertices.forEach { vertex ->
                DraggableVertex(vertex, scaleFactor, offsetFactor, interactionMode, viewModel)
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun <V, E>DraggableVertex(vertex: VertexModel<V>, scaleFactor: Float, offsetFactor: Offset, interactionMode: GraphInteractionMode, viewModel: GraphViewModel<V, E>) {
    var offsetX by remember { mutableStateOf(vertex.x * scaleFactor + offsetFactor.x) }
    var offsetY by remember { mutableStateOf(vertex.y * scaleFactor + offsetFactor.y) }
    val vertexSize by remember { mutableStateOf(VERTEX_SIZE*scaleFactor) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .size(vertexSize.dp)
            .graphicsLayer {
                translationX = -vertexSize
                translationY = -vertexSize
            }
            .background(MaterialTheme.colors.primary.copy(alpha = VERTEX_ALPHA), shape = CircleShape)
            .pointerInput(Unit) {
                if (interactionMode != GraphInteractionMode.Drag) return@pointerInput
                detectDragGestures { change, dragAmount ->
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                    viewModel.updateGraph {
                        it.vertexSet().forEach {v ->
                            if (v.id == vertex.id) {
                                v.x = (offsetX - offsetFactor.x) / scaleFactor
                                v.y = (offsetY - offsetFactor.y) / scaleFactor
                            }
                        }
                    }
                }
            }
            .clickable {
                if (interactionMode != GraphInteractionMode.Delete) return@clickable
                viewModel.updateGraph {
                    it.removeVertex(vertex)
                }
            }
    )
}
