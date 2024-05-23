package display.stronglyConnectedComponentSearch

import graph.Graph

interface GraphSCCSearch<V, E> {
    /**
     * Returns a set of sets with vertices that are strongly connected
     */
    fun getSCCs(graph: Graph<V, E>) : Set<Set<V>>
}
