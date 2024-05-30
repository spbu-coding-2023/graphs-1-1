package display.pathSearch.implementation

import display.pathSearch.GraphPathSearch
import graph.Graph
import java.util.PriorityQueue

class GraphDijkstraPathFinder : GraphPathSearch {

    override fun <V, E> searchPath(graph: Graph<V, E>, startingVertex: V, endingVertex: V): List<V> {
        val distance = mutableMapOf<V, Double>()
        val predecessor = mutableMapOf<V, V?>()
        val vertexEdges = graph.edgeSetOfVertices()
        val path = mutableListOf<V>()

        val priorityQueue = PriorityQueue<Pair<V, Double>>(compareBy { it.second })

        for (vertex in graph.vertexSet()) {
            distance[vertex] = Double.POSITIVE_INFINITY
            predecessor[vertex] = null
        }

        distance[startingVertex] = 0.0
        priorityQueue.add(Pair(startingVertex, 0.0))

        while (priorityQueue.isNotEmpty()) {
            val (currentVertex, currentDistance) = priorityQueue.poll()

            if (currentDistance > distance[currentVertex]!!) continue

            for ((edgeTail, edgeHead) in vertexEdges) {
                if (edgeTail == currentVertex) {
                    val neighbor = edgeHead
                    val edgeWeight = graph.getEdgeWeight(edgeTail, neighbor)
                    val newDistance = distance[currentVertex]!! + edgeWeight

                    if (newDistance < distance[neighbor]!!) {
                        distance[neighbor] = newDistance
                        predecessor[neighbor] = currentVertex
                        priorityQueue.add(Pair(neighbor, newDistance))
                    }
                }
            }
        }

        if (predecessor[endingVertex] == null && startingVertex != endingVertex) {
            return listOf()
        }

        var currentVertex = endingVertex
        while (currentVertex != startingVertex) {
            if (predecessor[currentVertex] == null) {
                return listOf()
            }
            path.add(currentVertex)
            currentVertex = predecessor[currentVertex]!!
        }
        path.add(startingVertex)

        return path.reversed()
    }
}
