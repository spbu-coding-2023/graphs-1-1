package display.stronglyConnectedComponentSearch

import graph.Graph

interface GraphSCCSearch {
    /**
     * Returns a set of sets with vertices that are strongly connected
     */
    fun <V, E> getSCCs(graph: Graph<V, E>) : Set<Set<V>>?
}
