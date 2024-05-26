package repository

import graph.Graph
import java.io.File

interface DatabaseGraphImporter {
    /**
     * Imports a graph with a certain id within a database
     */
    fun <V, E>importGraph(graphId: String, credentials: List<String>): Graph<V, E>
}
