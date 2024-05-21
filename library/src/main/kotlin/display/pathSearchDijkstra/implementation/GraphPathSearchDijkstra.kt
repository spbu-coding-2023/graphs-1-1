package display.pathSearchDijkstra.implementation

import display.pathSearchDijkstra.GraphPathSearchDijkstra
import graph.Graph

class GraphDijkstraPathFinder<V, E> {

    override fun <V, E> searchPathDijkstra(graph: Graph<V, E>, startingVertex: V, endingVertex: V): List<V> {
        val distance = mutableMapOf<V, Double>()
        val predecessor = mutableMapOf<V, V?>()
        val unvisitedVertices = mutableSetOf<V>()

        for (vertex in graph.vertexSet()) {
            distance[vertex] = Double.POSITIVE_INFINITY
            predecessor[vertex] = null
            unvisitedVertices.add(vertex)
        }

        distance[startingVertex] = 0.0

        while (unvisitedVertices.isNotEmpty()) {
            val currentVertex = unvisitedVertices.minByOrNull { distance[it]!! } ?: break
            unvisitedVertices.remove(currentVertex)

            for (edge in graph.outgoingEdgesOf(currentVertex)) {
                val neighbor = graph.getEdgeHead(edge)
                val edgeWeight = graph.getEdgeWeight(edge)
                val newDistance = distance[currentVertex]!! + edgeWeight

                if (newDistance < distance[neighbor]!!) {
                    distance[neighbor] = newDistance
                    predecessor[neighbor] = currentVertex
                }
            }
        }

        val path = mutableListOf<V>()
        var currentVertex = endingVertex
        while (currentVertex != startingVertex) {
            path.add(currentVertex)
            currentVertex = predecessor[currentVertex] ?: break
        }
        path.add(startingVertex)

        return path.reversed()
    }
}
