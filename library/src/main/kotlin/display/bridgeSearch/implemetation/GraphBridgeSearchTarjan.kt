package display.bridgeSearch.implementation

import display.bridgeSearch.GraphBridgeSearch
import graph.Graph

class GraphTarjanBridgeFinder<V, E> : GraphBridgeSearch {
    private var time = 0

    override fun <V, E> getBridges(graph: Graph<V, E>): List<Pair<V, V>> {
        val vertices = graph.vertexSet()
        val disc = mutableMapOf<V, Int>()
        val low = mutableMapOf<V, Int>()
        val parent = mutableMapOf<V, V?>()
        val bridges = mutableListOf<Pair<V, V>>()

        for (v in vertices) {
            disc[v] = -1
            low[v] = -1
            parent[v] = null
        }

        for (v in vertices) {
            if (disc[v] == -1) {
                dfs(graph, v, disc, low, parent, bridges)
            }
        }

        return bridges
    }

    private fun <V, E> dfs(
        graph: Graph<V, E>,
        u: V,
        disc: MutableMap<V, Int>,
        low: MutableMap<V, Int>,
        parent: MutableMap<V, V?>,
        bridges: MutableList<Pair<V, V>>
    ) {
        disc[u] = time
        low[u] = time
        time++

        for (e in graph.outgoingEdgesOf(u)) {
            val v = graph.getEdgeHead(e)
            if (disc[v] == -1) {
                parent[v] = u
                dfs(graph, v, disc, low, parent, bridges)
                low[u] = minOf(low[u]!!, low[v]!!)

                if (low[v]!! > disc[u]!!) {
                    bridges.add(Pair(u, v))
                }
            } else if (v != parent[u]) {
                low[u] = minOf(low[u]!!, disc[v]!!)
            }
        }
    }
}
