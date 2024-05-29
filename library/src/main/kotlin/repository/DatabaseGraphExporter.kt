package repository

import graph.Graph
import java.io.File

interface DatabaseGraphExporter {
    /**
     * Exports graph to a file, needs credentials.
     * Outputs a unique string graphId that is used to access everything (all nodes and edges have this same graphId)
     */
    fun <V, E>exportGraph(graph: Graph<V, E>, credentials: List<String?>, graphId: String): String
}