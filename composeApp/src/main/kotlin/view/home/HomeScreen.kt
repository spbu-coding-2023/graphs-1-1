package view.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import graph.Graph
import graph.implementation.DirectedWeightedGraph
import graph.implementation.UndirectedWeightedGraph
import model.EdgeModel
import model.VertexModel
import view.home.columns.ColumnGraphsList
import view.home.columns.ColumnMenuList
import view.home.header.HeaderLogo
import view.settings.SettingsScreen
import view.workspace.WorkspaceScreen
import viewModel.workspace.graph.GraphViewModel
import viewModel.workspace.graph.GraphsContainerViewModel
import viewModel.workspace.graph.utils.LocalDatabase
import java.lang.Math.pow

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val graphsContainerViewModel by rememberUpdatedState(GraphsContainerViewModel())
        LocalDatabase.getAllGraphs().forEach { g ->
            // TODO: loads each time going to home screen, shouldn't
            graphsContainerViewModel.addGraph(g)
        }

        Column {
            HeaderLogo()

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                ColumnMenuList(modifier = Modifier.fillMaxWidth(1-.618f), graphsContainerViewModel)
                ColumnGraphsList(modifier = Modifier.weight(1f), graphsContainerViewModel)
            }

        }
    }
}
