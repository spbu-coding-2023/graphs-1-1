package view.workspace.graph.graphEdges

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onSizeChanged
import viewModel.workspace.graph.GraphEdgesViewModel

@Composable
fun GraphEdgesView(graphEdgesViewModel: GraphEdgesViewModel) {
    var canvasWidth by remember { mutableStateOf(0f) }
    var canvasHeight by remember { mutableStateOf(0f) }
    val scale by remember { mutableStateOf(graphEdgesViewModel.scale) }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged {size ->
                canvasWidth = size.width.toFloat()
                canvasHeight = size.height.toFloat()
            }
    ) {
        for ((p1, p2) in graphEdgesViewModel.edgePlacement) {
            drawLine(
                brush = SolidColor(Color.Red),
                Offset(p1.first*scale+canvasWidth/2, p1.second*scale+canvasHeight/2),
                Offset(p2.first*scale+canvasWidth/2,p2.second*scale+canvasHeight/2)
            )
        }
    }
}