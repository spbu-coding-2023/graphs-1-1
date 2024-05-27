package viewModel.workspace.graph

import androidx.lifecycle.ViewModel
import graph.Graph
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import model.EdgeModel
import model.VertexModel

/**
 * single container for all graphs the app has.
 * handles creation, deletion, and other operations of the graphs.
 * the graphs are locally stored
 */
class GraphsContainerViewModel : ViewModel() {
    private val _graphsContainer = MutableStateFlow<List<GraphViewModel>>(listOf())
    val graphsContainer: StateFlow<List<GraphViewModel>> = _graphsContainer


    private fun isGraphNameValid(graphName: String): Boolean {
        return true // TODO
    }

    fun addGraph(graphViewModel: GraphViewModel): Result<String> {
//        if (!isGraphNameValid(graphName)) return Result.failure(IllegalArgumentException("Invalid graph name"))
//
//        for (g in graphsContainer) {
//            if (g.graphName == graphName) return Result.failure(IllegalArgumentException("Graph with this name already exists"))
//        }
//
//        val graphViewModel = GraphViewModel(graph, graphName)

        _graphsContainer.value += graphViewModel

        return Result.success("Successfully created a new graph")
    }

    fun deleteGraph(graphName: String): Result<String> {
        val graphToDelete = _graphsContainer.value.find { it.graphName == graphName }
        if (graphToDelete == null) return Result.failure(IllegalArgumentException("Graph with this name does not exist"))

        val deletionResult = graphToDelete.storage.deleteGraph()

        if (!deletionResult) return Result.failure(IllegalArgumentException("Could not delete file of the graph $graphName"))

        _graphsContainer.value = _graphsContainer.value.filter { it != graphToDelete }

        return Result.success("Successfully deleted the graph $graphName")
    }

}