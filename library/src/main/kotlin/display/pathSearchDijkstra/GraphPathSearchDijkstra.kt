package display.pathSearchDijkstra

import graph.Graph

interface GraphPathSearchDijkstra {
    fun <V, E> searchPathDijkstra(graph: Graph<V, E>, startingVertex: V, endingVertex: V): List<V>
}