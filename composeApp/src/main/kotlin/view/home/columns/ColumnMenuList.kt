package view.home.columns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import graph.implementation.DirectedWeightedGraph
import graph.implementation.UndirectedWeightedGraph
import model.EdgeModel
import model.VertexModel
import view.create.CreateScreen
import view.home.setupCycle1
import view.settings.SettingsScreen
import viewModel.workspace.graph.GraphViewModel
import viewModel.workspace.graph.GraphsContainerViewModel

@Composable
fun ColumnMenuList(modifier: Modifier, graphsContainerViewModel: GraphsContainerViewModel) {
    val navigator = LocalNavigator.currentOrThrow

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .then(modifier)

    ) {
        ButtonMenuItem(
            text = "New Graph",
            onClick = {
                navigator.push(CreateScreen(graphsContainerViewModel))
            }
        )

        ButtonMenuItem(
            text = "Settings",
            onClick = {
                navigator.push(SettingsScreen())
            }
        )
    }
}
