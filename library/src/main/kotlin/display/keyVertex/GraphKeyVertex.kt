package display.keyVertex

import graph.Graph

interface GraphKeyVertex {
    /**
     * Returns map where each vertex mapped to float, which represents how valuable this vertex
     */
    fun <V, E>getKeyVertecies(graph: Graph<V, E>): Map<V, Float>
}
