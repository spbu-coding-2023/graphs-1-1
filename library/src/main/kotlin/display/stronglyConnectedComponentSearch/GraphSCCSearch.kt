package display.stronglyConnectedComponentSearch

import graph.Graph

interface GraphSCCSearch {
    /**
     * Returns a list of lists with vertices that are strongly connected
     */
    fun <V, E> getSCCs(graph: Graph<V, E>) : List<List<V>>
}
