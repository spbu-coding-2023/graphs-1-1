package view.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import graph.Graph
import graph.implementation.DirectedWeightedGraph
import model.EdgeModel
import model.VertexModel
import view.settings.SettingsScreen
import view.workspace.WorkspaceScreen
import viewModel.workspace.graph.GraphViewModel

fun setupCycle1(graph: Graph<VertexModel<Int>, EdgeModel<String>>) {
    val vv = mutableListOf<VertexModel<Int>>()
    for (i in 0..5) {
        vv.add(VertexModel(i, (0..100).random().toFloat(), (0..100).random().toFloat(), i*2))
        graph.addVertex(vv.last())
    }

    for (i in 6..15) {
        vv.add(VertexModel(i, (0..100).random().toFloat(), (0..100).random().toFloat(), i*2))
        graph.addVertex(vv.last())
        val ri = (0..i).random()
        graph.addEdge(vv[i], vv[ri], EdgeModel(i, ri, "$i"))
    }
}


class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val myGraph = DirectedWeightedGraph<VertexModel<Int>, EdgeModel<String>>()
        setupCycle1(myGraph)
        Column {
            Text("this is a home screen")
            Button(
                onClick = {
                    navigator.push(WorkspaceScreen(GraphViewModel(myGraph)))
                },
            ) {
                Text("Go to graphs!")
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
