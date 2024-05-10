package display.pathSearch.implementation

import display.pathSearch.GraphPathSearch
import graph.Graph
import kotlin.math.tan

class GraphPathSearchBellmanFord : GraphPathSearch {
    override fun <V, E> searchPath(graph: Graph<V, E>, startingVertex: V, endingVertex: V): List<V> {
        val distance = mutableMapOf<V, Double>()
        val predecessor = mutableMapOf<V, V?>()
        val vertecies = graph.vertexSet()
        val edges = graph.edgeSet()
        val path = listOf<V>()

        for (v in vertecies) {
            distance[v] = Double.POSITIVE_INFINITY
            predecessor[v] = null
        }

        distance[startingVertex] = 0.0

        repeat(vertecies.size-1) {
            for (edge in edges) {
                val edgeTail = graph.getEdgeTail(edge)
                val edgeHead = graph.getEdgeHead(edge)
                val weight = graph.getEdgeWeight(edge)

                if (distance[edgeTail]!! + weight < distance[edgeHead]!!) {
                    distance[edgeHead] = distance[edgeTail]!! + weight
                    predecessor[edgeHead] = edgeTail
                }
            }
        }

        for (edge in edges) { // TODO: with undirected graph will only take edges in one direction, bug
            val edgeTail = graph.getEdgeTail(edge)
            val edgeHead = graph.getEdgeHead(edge)
            val weight = graph.getEdgeWeight(edge)

            if (distance[edgeTail]!! + weight < distance[edgeHead]!!) {
                // negative cycle
                throw Error("Can not find path between vertecies: Negative cycle found") // TODO: bug, input graph must be without negative cycles ig
            }
        }

        var currentVertex = endingVertex
        while (currentVertex != startingVertex) {
            if (predecessor[currentVertex] == null) return listOf() // TODO: think of
            currentVertex = predecessor[currentVertex]!!
            path.addLast(currentVertex)
        }
        path.addFirst(endingVertex)
        return path.reversed()
    }
}