package repository

import graph.Graph
import java.io.File

interface GraphImporter {
    /**
     * Imports from file to graph
     */
    fun <V, E>importGraph(graph: Graph<V, E>, file: File)
}
