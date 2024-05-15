package display.pathSearch.implementation

import display.pathSearch.GraphPathSearch
import graph.Graph

class GraphPathSearchBellmanFord : GraphPathSearch {
    override fun <V, E> searchPath(graph: Graph<V, E>, startingVertex: V, endingVertex: V): List<V> {
        val distance = mutableMapOf<V, Double>()
        val predecessor = mutableMapOf<V, V?>()
        val vertices = graph.vertexSet()
        val edges = graph.edgeSet()
        val path = mutableListOf<V>()

        for (v in vertices) {
            distance[v] = Double.POSITIVE_INFINITY
            predecessor[v] = null
        }

        distance[startingVertex] = 0.0

        repeat(vertices.size-1) {
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
                throw Error("Can not find path between vertices: Negative cycle found") // TODO: bug, input graph must be without negative cycles ig
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