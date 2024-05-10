package display.cycleSearch

import graph.Graph

interface GraphVertexCycleSearch {
    /**
     * Returns all graph walks which are cycle and trail and contain startingVertex
     */
    fun <V, E>getTrailCycles(graph: Graph<V, E>, startingVertex: V): List<List<V>>

    /**
     * Returns all graph walks which are cycle and path and startingVertex
     */
    fun <V, E>getPathCycles(graph: Graph<V, E>, startingVertex: V): List<List<V>>
}