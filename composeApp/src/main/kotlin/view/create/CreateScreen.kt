package view.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import graph.implementation.DirectedUnweightedGraph
import graph.implementation.DirectedWeightedGraph
import graph.implementation.UndirectedUnweightedGraph
import graph.implementation.UndirectedWeightedGraph
import view.common.CommonScreenLayout
import view.common.OptionChoseRadio
import view.workspace.WorkspaceScreen
import viewModel.workspace.graph.GraphViewModel
import viewModel.workspace.graph.GraphsContainerViewModel

class CreateScreen(val viewModel: GraphsContainerViewModel) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var isWeighted by remember { mutableStateOf(true) }
        var isDirected by remember { mutableStateOf(true) }
        var graphName by remember { mutableStateOf("New graph") }

        CommonScreenLayout(
            title = "Create New Graph",
            content = {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text("Give a name to your new graph")
                        OutlinedTextField(
                            value = graphName,
                            onValueChange = { graphName = it },
                            label = { Text("Name") }
                        )

                        Text("Graph direction type")
                        OptionChoseRadio(
                            chooses = listOf(Pair(true, "Directed"), Pair(false, "Undirected")),
                            onChoseChange = { chose ->
                                isDirected = chose
                            }
                        )

                        Text("Graph weightiness type")
                        OptionChoseRadio(
                            chooses = listOf(Pair(true, "Weighted"), Pair(false, "Unweighted")),
                            onChoseChange = { chose ->
                                isWeighted = chose
                            }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                    ) {
                        OutlinedButton(
                            onClick = {
                                navigator.pop()
                            }
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                val newGraph = GraphViewModel(
                                    if (isDirected) if (isWeighted) DirectedWeightedGraph() else DirectedUnweightedGraph() else if (isWeighted) UndirectedWeightedGraph() else UndirectedUnweightedGraph(),
                                    graphName
                                )
                                viewModel.addGraph(newGraph)
                                navigator.push(WorkspaceScreen(newGraph))
                            }
                        ) {
                            Text("Create")
                        }
                    }
                }
            }
        )
    }
}