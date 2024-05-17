package display.bridgeSearch

import graph.Graph

interface GraphBridge {
    /**
     * Returns map where each vertex mapped to float, which represents how valuable this vertex
     */
    fun <V, E>getBridges(graph: Graph<V, E>): List<Pair<V, V>>
}