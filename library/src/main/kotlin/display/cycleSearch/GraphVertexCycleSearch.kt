package display.cycleSearch

import graph.Graph

interface GraphVertexCycleSearch {
    /**
     * Returns a graph walk which is cycle and trail and contains startingVertex
     */
    fun <V, E>getCycle(graph: Graph<V, E>, startingVertex: V): List<V>
}