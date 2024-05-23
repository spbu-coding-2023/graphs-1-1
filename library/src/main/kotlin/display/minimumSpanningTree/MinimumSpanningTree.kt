package display.minimumSpanningTree

import graph.Graph

interface GraphMST <V, E> {
    /**
     * Returns a list of edges that form the MST
     */
    fun getMST(graph: Graph<V, E>) : List<E>
}
