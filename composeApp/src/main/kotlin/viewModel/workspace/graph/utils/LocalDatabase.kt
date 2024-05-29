package viewModel.workspace.graph.utils

import graph.implementation.DirectedWeightedGraph
import model.EdgeModel
import model.VertexModel
import viewModel.workspace.graph.GraphViewModel
import java.io.File

/**
 * this thing know where all graphs are and can read them, delete them, update them, etc. singleton
 */
object LocalDatabase {
    var sqlitePath: String? = null
    var neo4jUrl: String? = null
    var neo4jUser: String? = null
    var neo4jPassword: String? = null

    fun getGraphsDirectory(): File {
        return File(System.getProperty("user.home"), ".graphses")
    }

    fun getAllGraphs(): List<GraphViewModel> {
        val dir = getGraphsDirectory()
        val dirFiles = dir.listFiles()
        val graphs = mutableListOf<GraphViewModel>()

        if (dirFiles != null) {
            for (gFile in dirFiles) {
                if (gFile.name.startsWith(".")) continue
                val newGraph = GraphViewModel(DirectedWeightedGraph<VertexModel, EdgeModel>(), gFile.name) //TODO: only supports strings

//                newGraph.storage.importGraph()

                graphs.add(newGraph)
            }
            return graphs
        } else {
            // dir is not a proper directory // TODO: check directory
            return graphs
        }
    }
}