package repository

import graph.Graph
import java.io.File

interface GraphExporter {
    /**
     * Exports graph to a file
     */
    fun <V, E>exportGraph(graph: Graph<V, E>, file: File)
}