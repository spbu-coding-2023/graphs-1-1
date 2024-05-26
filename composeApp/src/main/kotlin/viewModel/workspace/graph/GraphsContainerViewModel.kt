package viewModel.workspace.graph

import androidx.lifecycle.ViewModel
import graph.Graph
import model.EdgeModel
import model.VertexModel

/**
 * single container for all graphs the app has.
 * handles creation, deletion, and other operations of the graphs.
 * the graphs are locally stored
 */
class GraphsContainerViewModel : ViewModel() {
    private val graphsContainer: MutableList<GraphViewModel<*, *>> = mutableListOf()

    private fun isGraphNameValid(graphName: String): Boolean {
        return true // TODO
    }

    fun <V, E>addGraph(graph: Graph<VertexModel<V>, EdgeModel<E>>, graphName: String): Result<String> {
        if (!isGraphNameValid(graphName)) return Result.failure(IllegalArgumentException("Invalid graph name"))

        for (g in graphsContainer) {
            if (g.graphName == graphName) return Result.failure(IllegalArgumentException("Graph with this name already exists"))
        }

        val graphViewModel = GraphViewModel(graph, graphName)

        graphsContainer.add(graphViewModel)

        return Result.success("Successfully created a new graph")
    }

    fun deleteGraph(graphName: String): Result<String> {
        val graphToDelete = graphsContainer.find { it.graphName == graphName }
        if (graphToDelete == null) return Result.failure(IllegalArgumentException("Graph with this name does not exist"))

        val deletionResult = graphToDelete.storage.deleteGraph()

        if (!deletionResult) return Result.failure(IllegalArgumentException("Could not delete file of the graph $graphName"))

        graphsContainer.remove(graphToDelete)

        return Result.success("Successfully deleted the graph $graphName")
    }

}