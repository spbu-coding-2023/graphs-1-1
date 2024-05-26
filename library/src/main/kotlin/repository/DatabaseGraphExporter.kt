package repository

import graph.Graph
import java.io.File

interface DatabaseGraphExporter {
    /**
     * Exports graph to a file, needs credentials. Outputs a unique string that is used to access this graph in the database
     */
    fun <V, E>exportGraph(graph: Graph<V, E>, credentials: List<String>): String
}