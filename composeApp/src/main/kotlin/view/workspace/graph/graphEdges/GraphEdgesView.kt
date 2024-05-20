package view.workspace.graph.graphEdges

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.layout.onSizeChanged
import viewModel.workspace.graph.GraphEdgesViewModel

@Composable
fun <V, E>GraphEdgesView(graphEdgesViewModel: GraphEdgesViewModel<V, E>) {
    var canvasWidth by remember { mutableStateOf(0f) }
    var canvasHeight by remember { mutableStateOf(0f) }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged {size ->
                canvasWidth = size.width.toFloat()
                canvasHeight = size.height.toFloat()
            }
    ) {
        drawLine(
            brush = SolidColor(Color.Red),
            Offset(0f, 0f),
            Offset(canvasWidth,canvasHeight)
        )
    }
}