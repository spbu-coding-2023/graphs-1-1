package view.workspace.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()

//        val DEFAULT_GRAPH_WIDTH = 1000f
//        val DEFAULT_GRAPH_HEIGHT = 1000f
//        fun calculateScale(): Float = minOf(width / DEFAULT_GRAPH_WIDTH, height / DEFAULT_GRAPH_HEIGHT)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .onPointerEvent(PointerEventType.Scroll) {
                    val scaleChange = (scaleFactor * exp(it.changes.first().scrollDelta.y.sign * 0.2f)).coerceIn(0.25f, 10f)
                    viewModel.setScaleFactor(scaleChange)
                    viewModel.setOffsetFactor(offsetFactor*scaleChange)
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            viewModel.setOffsetFactor(offsetFactor + dragAmount)
                            println(dragAmount)
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
                                color = Color.Black.copy(alpha = 1f),
                                start = Offset(tailVertex.x * scaleFactor + offsetFactor.x, tailVertex.y * scaleFactor + offsetFactor.y),
                                end = Offset(headVertex.x * scaleFactor + offsetFactor.x, headVertex.y * scaleFactor + offsetFactor.y),
                                strokeWidth = .3f*scaleFactor
                            )
                        }
                    }
                }
                // Overlay vertices on top of the canvas
                vertices.forEach { vertex ->
                    DraggableVertex(vertex, viewModel)
                }
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

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) } // offsetX.roundToInt(), offsetY.roundToInt()
            .size((6*scaleFactor).dp)
            .background(Color.Blue.copy(alpha = 1f), shape = CircleShape)
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

fun Offset.rotateBy(angle: Float): Offset {
    val angleInRadians = angle * PI / 180
    return Offset(
        (x * cos(angleInRadians) - y * sin(angleInRadians)).toFloat(),
        (x * sin(angleInRadians) + y * cos(angleInRadians)).toFloat()
    )
}
