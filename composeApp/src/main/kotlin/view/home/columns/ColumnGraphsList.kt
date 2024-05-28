package view.home.columns

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import viewModel.workspace.graph.GraphsContainerViewModel

@Composable
fun ColumnGraphsList(modifier: Modifier, graphsContainerViewModel: GraphsContainerViewModel) {
    val graphsContainer by graphsContainerViewModel.graphsContainer.collectAsState()
    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .then(modifier),
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(0.dp, 8.dp)
        ) {
            graphsContainer.forEach { graph ->
                GraphRepresentation(
                    viewModel = graph,
                    graphsContainerViewModel = graphsContainerViewModel,
                    titleColor = MaterialTheme.colorScheme.primary,
                    subTitleColor = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
