package view.workspace.graph.graphVertecies

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onSizeChanged
import viewModel.workspace.graph.GraphEdgesViewModel
import viewModel.workspace.graph.GraphVertexViewModel

@Composable
fun GraphVerteciesView(graphVertexViewModel: GraphVertexViewModel) {
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
        for (v in graphVertexViewModel.verteciesPlacement) {
            drawCircle(
                brush = SolidColor(Color.Blue),
                center = Offset(v.first*graphVertexViewModel.scale+canvasWidth/2, v.second*graphVertexViewModel.scale+canvasHeight/2),
                radius = graphVertexViewModel.radiusScale*graphVertexViewModel.scale
            )
        }
    }
}