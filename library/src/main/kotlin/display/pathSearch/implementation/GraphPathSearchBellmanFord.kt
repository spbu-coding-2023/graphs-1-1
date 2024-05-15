package display.pathSearch.implementation

import display.pathSearch.GraphPathSearch
import graph.Graph

/**
 * Returns empty list if negative cycle found, otherwise a vertex path containing starting and ending points
 */
class GraphPathSearchBellmanFord : GraphPathSearch {
    override fun <V, E> searchPath(graph: Graph<V, E>, startingVertex: V, endingVertex: V): List<V> {
        val distance = mutableMapOf<V, Double>()
        val predecessor = mutableMapOf<V, V?>()
        val vertecies = graph.vertexSet()
        val vertexEdges = graph.edgeSetOfVertecies()
        val path = mutableListOf<V>()

        for (v in vertecies) {
            distance[v] = Double.POSITIVE_INFINITY
            predecessor[v] = null
        }

        distance[startingVertex] = 0.0

        repeat(vertecies.size-1) {
            for ((edgeTail, edgeHead) in vertexEdges) {
                val weight = graph.getEdgeWeight(edgeTail, edgeHead)

                if (distance[edgeTail]!! + weight < distance[edgeHead]!!) {
                    distance[edgeHead] = distance[edgeTail]!! + weight
                    predecessor[edgeHead] = edgeTail
                }
            }
        }

        for ((edgeTail, edgeHead) in vertexEdges) {
            val weight = graph.getEdgeWeight(edgeTail, edgeHead)

            if (distance[edgeTail]!! + weight < distance[edgeHead]!!) {
                // negative cycle
                return listOf()
            }
        }

        var currentVertex = endingVertex
        while (currentVertex != startingVertex) {
            if (predecessor[currentVertex] == null) return listOf() // TODO: think of
            currentVertex = predecessor[currentVertex]!!
            path.add(currentVertex)
        }
        path.add(0, endingVertex)
        return path.reversed()
    }
}