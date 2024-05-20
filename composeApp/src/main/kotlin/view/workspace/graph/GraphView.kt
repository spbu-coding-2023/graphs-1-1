package view.workspace.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import model.VertexModel
import viewModel.workspace.graph.GraphViewModel
import kotlin.math.roundToInt

@Composable
fun <V, E>GraphView(viewModel: GraphViewModel<V, E>) {
    val vertices by viewModel.vertices.collectAsState()
    val edges by viewModel.edges.collectAsState()
    val scaleFactor by viewModel.scaleFactor.collectAsState()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()

        val DEFAULT_GRAPH_WIDTH = 100f
        val DEFAULT_GRAPH_HEIGHT = 100f
        val scale = minOf(width / DEFAULT_GRAPH_WIDTH, height / DEFAULT_GRAPH_HEIGHT)
        viewModel.setScaleFactor(scale)
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            key(scaleFactor) {
                // Draw edges on a single canvas
                Canvas(modifier = Modifier.fillMaxSize().background(Color.Green)) {
                    edges.forEach { edge ->
                        val tailVertex = vertices.find { it.id == edge.tailVertexId }
                        val headVertex = vertices.find { it.id == edge.headVertexId }
                        if (tailVertex != null && headVertex != null) {
                            drawLine(
                                color = Color.Black,
                                start = Offset(tailVertex.x * scaleFactor, tailVertex.y * scaleFactor),
                                end = Offset(headVertex.x * scaleFactor, headVertex.y * scaleFactor),
                                strokeWidth = 4f
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
    var offsetX by remember { mutableStateOf(vertex.x * scaleFactor) }
    var offsetY by remember { mutableStateOf(vertex.y * scaleFactor) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) } // offsetX.roundToInt(), offsetY.roundToInt()
            .size((2*scaleFactor).dp)
            .background(Color.Blue)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                    viewModel.updateGraph {
                        it.vertexSet().forEach {v ->
                            if (v.id == vertex.id) {
                                println(v)
                                v.x = offsetX / scaleFactor
                                v.y = offsetY / scaleFactor
                                println(v)
                            }
                        }
                    }
                }
            }
    )
}
