package display.bridgeSearch

import graph.Graph

interface GraphBridgeSearch {
    /**
     * Returns list of pair, each contains vertexes, between witch bridge is.
     */
    fun <V, E> getBridges(graph: Graph<V, E>): List<Pair<V, V>>
}
