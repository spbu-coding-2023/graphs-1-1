package view.home.columns

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import viewModel.workspace.graph.GraphsContainerViewModel

@Composable
fun ColumnGraphsList(modifier: Modifier, graphsContainerViewModel: GraphsContainerViewModel) {
    val graphsContainer by graphsContainerViewModel.graphsContainer.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .then(modifier)

    ) {
        graphsContainer.forEach { graph ->
            GraphRepresentation(
                viewModel = graph,
                graphsContainerViewModel = graphsContainerViewModel,
                titleColor = MaterialTheme.colorScheme.onPrimaryContainer,
                subTitleColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = .7f)
            )
        }
    }
}
