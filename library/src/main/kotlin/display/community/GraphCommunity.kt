package display.community

import graph.Graph

interface GraphCommunity {
    /**
     * Returns map where each vertex in community determined by int
     */
    fun <V, E>getCommunities(graph: Graph<V, E>): Map<V, Int>
}