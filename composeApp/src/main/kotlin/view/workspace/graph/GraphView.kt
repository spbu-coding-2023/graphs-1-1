package view.workspace.graph

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import model.EdgeModel
import model.VertexModel
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
                    viewModel
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
fun <V, E>DraggableVertex(vertex: VertexModel<V>, scaleFactor: Float, offsetFactor: Offset, graphViewModel: GraphViewModel<V, E>) {
    val scaleFactor by rememberUpdatedState(scaleFactor)
    val offsetFactor by rememberUpdatedState(offsetFactor)
    var offsetX by remember { mutableStateOf(vertex.x) }
    var offsetY by remember { mutableStateOf(vertex.y) }
    val vertexSize by rememberUpdatedState(VERTEX_SIZE*scaleFactor)
    val interactionMode by graphViewModel.interactionMode.collectAsState()
    var reRender by remember { mutableStateOf(false) }

    key (reRender) {
        Box(
            modifier = Modifier
                .offset { IntOffset((offsetX * scaleFactor + offsetFactor.x).roundToInt(), (offsetY * scaleFactor + offsetFactor.y).roundToInt()) }
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
                            when (interactionMode) {
                                GraphInteractionMode.Pan -> {
                                    graphViewModel.handlePan(dragAmount)
                                }
                                GraphInteractionMode.Drag -> {
                                    offsetX += dragAmount.x / scaleFactor
                                    offsetY += dragAmount.y / scaleFactor
                                    vertex.x = offsetX
                                    vertex.y = offsetY
//                                    graphViewModel.updateState()
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
                                graphViewModel.switchSelectionVertex(vertex)
                                reRender = !reRender
                            }
                            GraphInteractionMode.Delete -> {
                                graphViewModel.removeAndAllIfSelected(vertex)
                                reRender = !reRender
                            }
                            else -> return@detectTapGestures
                        }
                    }
                }
        )
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
                val startX = tailVertex.x * scaleFactor + offsetFactor.x
                val startY = tailVertex.y * scaleFactor + offsetFactor.y
                val endX = headVertex.x * scaleFactor + offsetFactor.x
                val endY  = headVertex.y * scaleFactor + offsetFactor.y

                if (startX == endX && startY == endY) {
                    drawSelfLoop(
                        centerX = startX,
                        centerY = startY,
                        vertexRadius = VERTEX_SIZE*scaleFactor,
                        arrowHeadLength = 20f*scaleFactor,
                        arrowHeadAngle = 30f,
                        lineColor = EDGE_COLOR.copy(alpha = EDGE_ALPHA),
                        lineWidth = EDGE_STROKE_WIDTH*scaleFactor
                    )
                } else if (edge.isDirected) {
                    drawArrow(
                        startX = startX,
                        startY = startY,
                        endX = endX,
                        endY = endY,
                        vertexRadius = VERTEX_SIZE*scaleFactor,
                        arrowHeadLength = 20f*scaleFactor,
                        arrowHeadAngle = 30f,
                        lineColor = EDGE_COLOR.copy(alpha = EDGE_ALPHA),
                        lineWidth = EDGE_STROKE_WIDTH*scaleFactor
                    )
                } else {
                    drawLine(
                        color = EDGE_COLOR.copy(alpha = EDGE_ALPHA),
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = EDGE_STROKE_WIDTH*scaleFactor
                    )
                }


            }
        }
    }
}

fun DrawScope.drawArrow(
    startX: Float,
    startY: Float,
    endX: Float,
    endY: Float,
    vertexRadius: Float,
    arrowHeadLength: Float,
    arrowHeadAngle: Float,
    lineColor: Color,
    lineWidth: Float
) {
    val angle = atan2(endY - startY, endX - startX)

    val startAdjustedX = startX + vertexRadius * cos(angle)
    val startAdjustedY = startY + vertexRadius * sin(angle)
    val endAdjustedX = endX - vertexRadius * cos(angle)
    val endAdjustedY = endY - vertexRadius * sin(angle)

    val arrowX1 = endAdjustedX - arrowHeadLength * cos(angle - Math.toRadians(arrowHeadAngle.toDouble())).toFloat()
    val arrowY1 = endAdjustedY - arrowHeadLength * sin(angle - Math.toRadians(arrowHeadAngle.toDouble())).toFloat()
    val arrowX2 = endAdjustedX - arrowHeadLength * cos(angle + Math.toRadians(arrowHeadAngle.toDouble())).toFloat()
    val arrowY2 = endAdjustedY - arrowHeadLength * sin(angle + Math.toRadians(arrowHeadAngle.toDouble())).toFloat()

    // Draw the main line
    drawLine(
        color = lineColor,
        start = Offset(startAdjustedX, startAdjustedY),
        end = Offset(endAdjustedX, endAdjustedY),
        strokeWidth = lineWidth
    )

    // Draw the arrowhead
    val arrowPath = Path().apply {
        moveTo(endAdjustedX, endAdjustedY)
        lineTo(arrowX1, arrowY1)
        lineTo(arrowX2, arrowY2)
        close()
    }
    drawPath(
        path = arrowPath,
        color = lineColor
    )
}


fun DrawScope.drawSelfLoop(
    centerX: Float,
    centerY: Float,
    vertexRadius: Float,
    arrowHeadLength: Float,
    arrowHeadAngle: Float,
    lineColor: Color,
    lineWidth: Float
) {
    val loopRadius = vertexRadius * 3.5f
    val controlOffset = loopRadius
    val controlX1 = centerX
    val controlY1 = centerY - controlOffset
    val controlX2 = centerX - controlOffset
    val controlY2 = centerY

    // Start from top center and end at the left center
    val startX = centerX
    val startY = centerY - vertexRadius
    val endX = centerX - vertexRadius
    val endY = centerY

    // Draw the loop using a cubic Bezier curve
    val loopPath = Path().apply {
        moveTo(startX, startY)
        cubicTo(controlX1, controlY1, controlX2, controlY2, endX, endY)
    }
    drawPath(loopPath, color = lineColor, style = Stroke(width = lineWidth))

    // Draw the arrowhead at the end of the loop
    val angle = atan2(controlY2 - endY, controlX2 - endX)
    val arrowX1 = endX - arrowHeadLength * cos(angle - Math.toRadians(arrowHeadAngle.toDouble())).toFloat()
    val arrowY1 = endY - arrowHeadLength * sin(angle - Math.toRadians(arrowHeadAngle.toDouble())).toFloat()
    val arrowX2 = endX - arrowHeadLength * cos(angle + Math.toRadians(arrowHeadAngle.toDouble())).toFloat()
    val arrowY2 = endY - arrowHeadLength * sin(angle + Math.toRadians(arrowHeadAngle.toDouble())).toFloat()

    // Mirror the arrowhead along the y-axis
    val mirroredArrowX1 = endX - (arrowX1 - endX)
    val mirroredArrowX2 = endX - (arrowX2 - endX)

    val arrowPath = Path().apply {
        moveTo(endX, endY)
        lineTo(mirroredArrowX1, arrowY1)
        lineTo(mirroredArrowX2, arrowY2)
        close()
    }
    drawPath(
        path = arrowPath,
        color = lineColor
    )
}
