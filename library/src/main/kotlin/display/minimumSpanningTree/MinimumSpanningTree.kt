package display.minimumSpanningTree

import graph.Graph

interface GraphMST {
    /**
     * Returns a list of edges that form the MST
     */
    fun <V, E> getMST(graph: Graph<V, E>) : Set<E>?
}
