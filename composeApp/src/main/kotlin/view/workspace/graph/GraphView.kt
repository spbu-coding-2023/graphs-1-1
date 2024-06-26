package view.workspace.graph

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import model.EdgeModel
import model.VertexModel
import viewModel.workspace.graph.GraphInteractionMode
import viewModel.workspace.graph.GraphViewModel
import kotlin.math.*
import kotlin.random.Random
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.sp

val MAX_SCALE_FACTOR = 5f
val MIN_SCALE_FACTOR = .1f

val EDGE_STROKE_WIDTH = 2f
val EDGE_ALPHA = 0.5f

val VERTEX_SIZE = 23f
val VERTEX_ALPHA = 1f

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GraphView(viewModel: GraphViewModel) {
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

        LaunchedEffect(Unit) {
            viewModel.setOffsetFactor(Offset(constraints.maxWidth.toFloat()/2, constraints.maxHeight.toFloat()/2))
        }
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
fun DraggableVertex(vertex: VertexModel, scaleFactor: Float, offsetFactor: Offset, graphViewModel: GraphViewModel) {
    val scaleFactor by rememberUpdatedState(scaleFactor)
    val offsetFactor by rememberUpdatedState(offsetFactor)
    var offsetX by remember { mutableStateOf(vertex.x) }
    var offsetY by remember { mutableStateOf(vertex.y) }
    val vertexSize by rememberUpdatedState(VERTEX_SIZE*scaleFactor*vertex.size)
    val interactionMode by graphViewModel.interactionMode.collectAsState()
    var reRender by remember { mutableStateOf(false) }

    val random = Random(vertex.communityId)
    val communityColorRed = random.nextInt(128)+128
    val communityColorGreen = random.nextInt(128)+128
    val communityColorBlue = random.nextInt(128)+128
    val communityColor = if (vertex.communityId == 0) MaterialTheme.colorScheme.surfaceDim else Color(communityColorRed, communityColorGreen, communityColorBlue)

    key (reRender) {
        Box(
            modifier = Modifier
                .offset { IntOffset((offsetX * scaleFactor + offsetFactor.x).roundToInt(), (offsetY * scaleFactor + offsetFactor.y).roundToInt()) }
                .size(vertexSize.dp)
                .graphicsLayer {
                    translationX = -vertexSize/2 * density
                    translationY = -vertexSize/2 * density
                    scaleX = 2/density
                    scaleY = 2/density
                }
                .background(
            color = (if (vertex.isSelected) MaterialTheme.colorScheme.surfaceBright else communityColor).copy(alpha = VERTEX_ALPHA),
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
                },
            contentAlignment = Alignment.Center
        ) {
            if (scaleFactor >= 1.4f) {
                Text(
                    text = vertex.id.toString(),
                    fontSize = MaterialTheme.typography.labelSmall.fontSize*scaleFactor,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = .8f)
                )
            }
        }
    }

}

@Composable
fun EdgesView(vertices: List<VertexModel>, edges: List<EdgeModel>, scaleFactor: Float, offsetFactor: Offset) {
    val EDGE_COLOR = MaterialTheme.colorScheme.outline.copy(alpha = EDGE_ALPHA)
    val EDGE_COLOR_HIGHLIGHTED = MaterialTheme.colorScheme.primary
    val EDGE_STROKE_WIDTH_HIGHLIGHTED = EDGE_STROKE_WIDTH * 2.5f
    val WEIGHT_SIZE = 12f
    val textMeasurer = rememberTextMeasurer()
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
                        vertexRadius = VERTEX_SIZE*scaleFactor*headVertex.size,
                        arrowHeadLength = 20f*scaleFactor,
                        arrowHeadAngle = 30f,
                        lineColor = (if (edge.isHighlighted) EDGE_COLOR_HIGHLIGHTED else EDGE_COLOR),
                        lineWidth = (if (edge.isHighlighted) EDGE_STROKE_WIDTH_HIGHLIGHTED else EDGE_STROKE_WIDTH)*scaleFactor
                    )
                } else if (edge.isDirected) {
                    drawArrow(
                        startX = startX,
                        startY = startY,
                        endX = endX,
                        endY = endY,
                        vertexRadiusStart = VERTEX_SIZE*scaleFactor*tailVertex.size,
                        vertexRadiusEnd = VERTEX_SIZE*scaleFactor*headVertex.size,
                        arrowHeadLength = 20f*scaleFactor,
                        arrowHeadAngle = 30f,
                        lineColor = (if (edge.isHighlighted) EDGE_COLOR_HIGHLIGHTED else EDGE_COLOR),
                        lineWidth = (if (edge.isHighlighted) EDGE_STROKE_WIDTH_HIGHLIGHTED else EDGE_STROKE_WIDTH)*scaleFactor
                    )
                } else {
                    drawLine(
                        color = (if (edge.isHighlighted) EDGE_COLOR_HIGHLIGHTED else EDGE_COLOR),
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = (if (edge.isHighlighted) EDGE_STROKE_WIDTH_HIGHLIGHTED else EDGE_STROKE_WIDTH)*scaleFactor
                    )
                }

                // draw weight
                if (edge.isWeighted) {
                    val textLayoutResult = textMeasurer.measure(
                        text = AnnotatedString(edge.weight.toString()),
                        style = TextStyle(
                            fontSize = (WEIGHT_SIZE*scaleFactor).sp,
                        )
                    )
                    val textX = (startX + endX)/2
                    val textY = (startY + endY)/2

                    drawText(
                        textLayoutResult = textLayoutResult,
                        brush = SolidColor(EDGE_COLOR),
                        topLeft = Offset(textX, textY)
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
    vertexRadiusStart: Float,
    vertexRadiusEnd: Float,
    arrowHeadLength: Float,
    arrowHeadAngle: Float,
    lineColor: Color,
    lineWidth: Float
) {
    val angle = atan2(endY - startY, endX - startX)

    val startAdjustedX = startX + vertexRadiusStart * cos(angle)
    val startAdjustedY = startY + vertexRadiusStart * sin(angle)
    val endAdjustedX = endX - vertexRadiusEnd * cos(angle)
    val endAdjustedY = endY - vertexRadiusEnd * sin(angle)

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
