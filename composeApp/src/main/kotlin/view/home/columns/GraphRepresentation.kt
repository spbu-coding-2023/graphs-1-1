package view.home.columns

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import view.home.columns.deleteDialog.DeleteGraphDialog
import view.workspace.WorkspaceScreen
import viewModel.workspace.graph.GraphViewModel
import viewModel.workspace.graph.GraphsContainerViewModel
import viewModel.workspace.graph.utils.LocalDatabase
import kotlin.math.abs

@Composable
fun GraphRepresentation(viewModel: GraphViewModel, graphsContainerViewModel: GraphsContainerViewModel, titleColor: Color, subTitleColor: Color) {
    var isDeleteDialogShown by remember { mutableStateOf(false) }
    val navigator = LocalNavigator.currentOrThrow
    val density = LocalDensity.current.density
    val colorTriples = listOf(
        Triple(Color(0xFF6200EE), Color(0xFFB07FF6), Color.White),
        Triple(Color(0xFFF44336), Color(0xFFFAB3AE), Color.White),
        Triple(Color(0xFF38761d), Color(0xFF9BBA8E), Color.White),
        Triple(Color(0xFFC90076), Color(0xFFE999C8), Color.White),
        Triple(Color(0xFF16537E), Color(0xFFD0DCE5), Color.White),
    )
    val colorTriple = colorTriples[abs(viewModel.graphName.hashCode()) % colorTriples.size]

    fun createLinearGradient(): Brush {
        return Brush.linearGradient(
            colors = listOf(colorTriple.first, colorTriple.second),
            start = Offset(0f, 0f),
            end = Offset(50f*density, 50f*density)
        )
    }

    var iconBgColors: Brush by mutableStateOf(createLinearGradient())
    var iconTextColor: Color by mutableStateOf(colorTriple.third)


    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Surface(
           modifier = Modifier
               .size(40.dp)
               .offset(y = 4.dp),
            shape = MaterialTheme.shapes.medium,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
                                   .background(brush = iconBgColors)
            ) {
                Text(
                    text = viewModel.graphName.slice(0..1).uppercase(),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = iconTextColor
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .clickable {
                    navigator.push(WorkspaceScreen(viewModel))
                },
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Bottom)
        ) {
            Text(
                text = viewModel.graphName,
                color = titleColor,
                fontSize = MaterialTheme.typography.labelMedium.fontSize
            )

            Text(
                text = "${LocalDatabase.getGraphsDirectory()}/${viewModel.graphName}",
                color = subTitleColor,
                fontSize = MaterialTheme.typography.labelSmall.fontSize
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            modifier = Modifier,
            onClick = {
                isDeleteDialogShown = !isDeleteDialogShown
            }
        ) {
            Icon(Icons.Filled.DeleteForever, "deleteIcon")
        }

    }

    if (isDeleteDialogShown) {
        DeleteGraphDialog(
            onDismissRequest = {
                isDeleteDialogShown = !isDeleteDialogShown
            },
            onConfirmRequest = {
                graphsContainerViewModel.deleteGraph(viewModel.graphName)
                isDeleteDialogShown = !isDeleteDialogShown
            }
        )
    }
}
