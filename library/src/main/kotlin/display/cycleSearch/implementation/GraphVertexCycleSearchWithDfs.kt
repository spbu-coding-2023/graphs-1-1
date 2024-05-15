package display.cycleSearch.implementation

import display.cycleSearch.GraphVertexCycleSearch
import graph.Graph

class GraphVertexCycleSearchWithDfs : GraphVertexCycleSearch {
    override fun <V, E> getTrailCycle(graph: Graph<V, E>, startingVertex: V): List<V> {
        val visitedEdges = HashSet<Pair<V, V>>() // TODO: look at the opposite edge direction as well in undirected graph
        val cameFrom = HashMap<V, V>()

        val stack = mutableListOf(startingVertex)

        while(stack.isNotEmpty()) {
            val currentVertex = stack.removeLast()

            for (neighbourVertex in graph.outgoingVerticesOf(currentVertex)) {
                val currentEdge = Pair(currentVertex, neighbourVertex)
                cameFrom[neighbourVertex] = currentVertex
                if (currentEdge !in visitedEdges) {
                    visitedEdges.add(currentEdge)
                    if (neighbourVertex == startingVertex && cameFrom[neighbourVertex] != startingVertex) {
                        val path = mutableListOf(startingVertex)
                        var cur = cameFrom[neighbourVertex]
                        while (cur != startingVertex) {
                            if (cur == null) break
                            path.add(cur)
                            cur = cameFrom[cur]
                        }
                        path.add(startingVertex)
                        return path.reversed()
                    }
                    stack.add(neighbourVertex)
                }
            }
        }

        return listOf()
    }

    override fun <V, E> getPathCycle(graph: Graph<V, E>, startingVertex: V): List<V> {
        val visitedVertices = HashSet<V>() // TODO: look at the opposite edge direction as well in undirected graph
        val cameFrom = HashMap<V, V>()

        val stack = mutableListOf(startingVertex)

        while(stack.isNotEmpty()) {
            val currentVertex = stack.removeLast()

            for (neighbourVertex in graph.outgoingVerticesOf(currentVertex)) {
                cameFrom[neighbourVertex] = currentVertex
                if (neighbourVertex !in visitedVertices) {
                    visitedVertices.add(neighbourVertex)
                    if (neighbourVertex == startingVertex && cameFrom[neighbourVertex] != startingVertex) {
                        val path = mutableListOf(startingVertex)
                        var cur = cameFrom[neighbourVertex]
                        while (cur != startingVertex) {
                            if (cur == null) break
                            path.add(cur)
                            cur = cameFrom[cur]
                        }
                        path.add(startingVertex)
                        return path.reversed()
                    }
                    stack.add(neighbourVertex)
                }
            }
        }

        return listOf()
    }
}