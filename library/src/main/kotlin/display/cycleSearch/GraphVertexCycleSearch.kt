package display.cycleSearch

import graph.Graph

interface GraphVertexCycleSearch {
    /**
     * Returns a graph walk which is cycle and trail and contains startingVertex
     */
    fun <V, E>getTrailCycle(graph: Graph<V, E>, startingVertex: V): List<V>

    /**
     * Returns a graph walk which is cycle and path and contains startingVertex
     */
    fun <V, E>getPathCycle(graph: Graph<V, E>, startingVertex: V): List<V>
}