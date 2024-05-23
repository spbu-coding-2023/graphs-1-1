package display.stronglyConnectedComponentSearch

import graph.Graph

interface GraphSCCSearch<V, E> {
    /**
     * Returns a list of lists with vertices that are strongly connected
     */
    fun getSCCs(graph: Graph<V, E>) : List<List<V>>
}
