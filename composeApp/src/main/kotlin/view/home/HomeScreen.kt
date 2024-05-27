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

fun setupCycle1(graph: Graph<VertexModel, EdgeModel>, n: Int) {
    val vv = mutableListOf<VertexModel>()
    val r = 1000

    for (i in 0..n) {
        vv.add(VertexModel(i, (0..r).random().toFloat(), (0..r).random().toFloat(), i.toString()))
        graph.addVertex(vv.last())
        repeat(2) {
            val ri = (pow((0..i).random().toDouble(), 2.952)/pow(i.toDouble(), 2.952)*i).toInt()
            graph.addEdge(vv[i], vv[ri], EdgeModel(i, ri, "$i"))
        }
    }
}


class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val graphsContainerViewModel by rememberUpdatedState(GraphsContainerViewModel())
        LocalDatabase.getAllGraphs().forEach { g ->
            // TODO: loads each time going to home screen, shouldn't
            graphsContainerViewModel.addGraph(g)
        }
        val graphs by graphsContainerViewModel.graphsContainer.collectAsState()

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




            Button(
                onClick = {
                    val gg = UndirectedWeightedGraph<VertexModel, EdgeModel>()
                    setupCycle1(gg, 10)
                    graphsContainerViewModel.addGraph(GraphViewModel(gg, "namelesss thing"))
                }
            ) { Text("add") }
            graphs.forEach { g ->
                Button(
                    onClick = {
                        navigator.push(WorkspaceScreen(g))
                    }
                ) {
                    Text("go to graph \"${g.graphName}\"")
                }
            }
            Button(
                onClick = {
                    navigator.push(SettingsScreen())
                },
            ) {
                Text("Go to settings!")
            }
        }
    }
}
