package display.pathSearch

import graph.Graph

interface GraphPathSearch {
    fun <V, E> searchPath(graph: Graph<V, E>, startingVertex: V, endingVertex: V): List<V>
}