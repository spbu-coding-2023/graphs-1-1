package view.workspace.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import model.VertexModel
import viewModel.workspace.graph.GraphViewModel
import kotlin.math.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <V, E>GraphView(viewModel: GraphViewModel<V, E>) {
    val vertices by viewModel.vertices.collectAsState()
    val edges by viewModel.edges.collectAsState()
    val scaleFactor by viewModel.scaleFactor.collectAsState()
    val offsetFactor by viewModel.offsetFactor.collectAsState()
    val rotationFactor by viewModel.rotationFactor.collectAsState()
//    viewModel.setScaleFactor(10f)
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()

        val DEFAULT_GRAPH_WIDTH = 1000f
        val DEFAULT_GRAPH_HEIGHT = 1000f
        val scale = minOf(width / DEFAULT_GRAPH_WIDTH, height / DEFAULT_GRAPH_HEIGHT)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onPointerEvent(PointerEventType.Scroll) {
                    val point = it.changes.first()
                    if (point.scrollDelta.y == 0f) return@onPointerEvent

                    val delta = max(1f, min(1.08f, exp(scaleFactor/100))) // zoom factor

                    val deltaZoom = if (point.scrollDelta.y > 0) delta else 1/delta
                    val scaleChange = scaleFactor * deltaZoom
                    if (scaleChange < 1f || scaleChange > 50f) return@onPointerEvent // zoom range

                    viewModel.setScaleFactor(scaleChange)
                    val offsetChange = point.position - (point.position - offsetFactor) * deltaZoom
                    viewModel.setOffsetFactor(offsetChange)
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            viewModel.setOffsetFactor(offsetFactor + dragAmount)
                        }
                    )
                }
        ) {
            key(scaleFactor, offsetFactor) {
                // Draw edges on a single canvas
                Canvas(modifier = Modifier.fillMaxSize().background(Color.Green)) {
                    edges.forEach { edge ->
                        val tailVertex = vertices.find { it.id == edge.tailVertexId }
                        val headVertex = vertices.find { it.id == edge.headVertexId }
                        if (tailVertex != null && headVertex != null) {
                            drawLine(
                                color = Color.Black.copy(alpha = .3f),
                                start = Offset(tailVertex.x * scaleFactor + offsetFactor.x, tailVertex.y * scaleFactor + offsetFactor.y),
                                end = Offset(headVertex.x * scaleFactor + offsetFactor.x, headVertex.y * scaleFactor + offsetFactor.y),
                                strokeWidth = .1f*scaleFactor
                            )
                        }
                    }
                }
                // Overlay vertices on top of the canvas
//                vertices.forEach { vertex ->
//                    DraggableVertex(vertex, viewModel)
//                }
            }
        }
    }
}

@Composable
fun <V, E>DraggableVertex(vertex: VertexModel<V>, viewModel: GraphViewModel<V, E>) {
    val scaleFactor by viewModel.scaleFactor.collectAsState()
    val offsetFactor by viewModel.offsetFactor.collectAsState()
    var offsetX by remember { mutableStateOf(vertex.x * scaleFactor + offsetFactor.x) }
    var offsetY by remember { mutableStateOf(vertex.y * scaleFactor + offsetFactor.y) }
    val vertexSize by remember { mutableStateOf(.5f*scaleFactor) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) } // offsetX.roundToInt(), offsetY.roundToInt()
            .size(vertexSize.dp)
            .graphicsLayer {
                translationX = -vertexSize/2
                translationY = -vertexSize/2
            }
            .background(Color.Blue.copy(alpha = .4f), shape = CircleShape)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
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
    )
}
