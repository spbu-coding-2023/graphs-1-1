package display.bridgeSearch.implementation

import display.bridgeSearch.GraphBridgeSearch
import graph.Graph

class GraphTarjanBridgeFinder : GraphBridgeSearch {

    override fun <V, E> getBridges(graph: Graph<V, E>): List<Pair<V, V>> {
        val vertices = graph.vertexSet()
        val discoveryTimes = mutableMapOf<V, Int>()
        val lowestDiscoveryTimes = mutableMapOf<V, Int>()
        val parents = mutableMapOf<V, V?>()
        val bridges = mutableListOf<Pair<V, V>>()
        var time = 0

        for (vertex in vertices) {
            discoveryTimes[vertex] = -1
            lowestDiscoveryTimes[vertex] = -1
            parents[vertex] = null
        }

        for (vertex in vertices) {
            if (discoveryTimes[vertex] == -1) {
                dfs(graph, vertex, discoveryTimes, lowestDiscoveryTimes, parents, bridges, time)
            }
        }

        return bridges
    }

    private fun <V, E> dfs(
        graph: Graph<V, E>,
        currentVertex: V,
        discoveryTimes: MutableMap<V, Int>,
        lowestDiscoveryTimes: MutableMap<V, Int>,
        parents: MutableMap<V, V?>,
        bridges: MutableList<Pair<V, V>>,
        time: Int
    ) {
        discoveryTimes[currentVertex] = time
        lowestDiscoveryTimes[currentVertex] = time
        var localTime = time + 1

        for (adjacentVertex in graph.outgoingVerticesOf(currentVertex)) {
            if (discoveryTimes[adjacentVertex] == -1) {
                parents[adjacentVertex] = currentVertex
                dfs(graph, adjacentVertex, discoveryTimes, lowestDiscoveryTimes, parents, bridges, localTime)
                lowestDiscoveryTimes[currentVertex] = minOf(lowestDiscoveryTimes[currentVertex]!!, lowestDiscoveryTimes[adjacentVertex]!!)

                if (lowestDiscoveryTimes[adjacentVertex]!! > discoveryTimes[currentVertex]!!) {
                    bridges.add(Pair(currentVertex, adjacentVertex))
                }
            } else if (adjacentVertex != parents[currentVertex]) {
                lowestDiscoveryTimes[currentVertex] = minOf(lowestDiscoveryTimes[currentVertex]!!, discoveryTimes[adjacentVertex]!!)
            }
        }
    }
}
