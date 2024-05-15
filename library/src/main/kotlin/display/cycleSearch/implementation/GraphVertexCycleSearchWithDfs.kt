package display.cycleSearch.implementation

import display.cycleSearch.GraphVertexCycleSearch
import graph.Graph
import java.util.LinkedList
import java.util.Queue

class GraphVertexCycleSearchWithDfs : GraphVertexCycleSearch {
//    override fun <V, E> getCasdasdycle(graph: Graph<V, E>, startingVertex: V): List<V> {
////        val visitedEdges = HashSet<Pair<V, V>>() // TODO: look at the opposite edge direction as well in undirected graph
//        val cameFrom = HashMap<V, V>()
//        val isDirected = graph.configuration.isDirected()
//        val stack = mutableListOf(startingVertex)
//
//        while(stack.isNotEmpty()) {
//            val currentVertex = stack.removeLast()
//            print("all: ")
//            println(graph.outgoingVerteciesOf(currentVertex))
//            for (neighbourVertex in graph.outgoingVerteciesOf(currentVertex)) {
//                val currentEdge = Pair(currentVertex, neighbourVertex)
//                cameFrom[neighbourVertex] = currentVertex
//                if (currentEdge !in visitedEdges) {
//                    visitedEdges.add(currentEdge)
//                    if (!isDirected) visitedEdges.add(Pair(currentEdge.second, currentEdge.first))
//                    println("stat")
//                    println(neighbourVertex)
//                    println(visitedEdges)
//                    if (neighbourVertex == startingVertex && cameFrom[neighbourVertex] != startingVertex) {
//                        val path = mutableListOf(startingVertex)
//                        var cur = cameFrom[neighbourVertex]
//                        while (cur != startingVertex) {
//                            if (cur == null) break
//                            path.add(cur)
//                            cur = cameFrom[cur]
//                        }
//                        path.add(startingVertex)
//                        return path.reversed()
//                    }
//                    stack.add(neighbourVertex)
//                }
//            }
//        }
//
//        return listOf()
//    }

    override fun <V, E> getCycle(graph: Graph<V, E>, startingVertex: V): List<V> {
        val isDirected = graph.configuration.isDirected()
        val cameFrom = HashMap<V, V>(graph.vertexSet().size)
        val visited = HashSet<V>(graph.vertexSet().size)
        val stack = mutableListOf(startingVertex)

        while (stack.isNotEmpty()) {
            val currentVertex = stack.removeLast()
            visited.add(currentVertex)
            for (neighbourVertex in graph.outgoingVerteciesOf(currentVertex)) {

                if (neighbourVertex == startingVertex) {
                    cameFrom[neighbourVertex] = currentVertex
                }

                if (neighbourVertex == startingVertex && (isDirected || cameFrom[currentVertex] != startingVertex)) {
                    // found path
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

                if (neighbourVertex !in visited) {
                    cameFrom[neighbourVertex] = currentVertex
                    stack.add(neighbourVertex)
                }
            }
        }

        return listOf()
    }
}