package display.cycleSearch.implementation

import display.cycleSearch.GraphVertexCycleSearch
import graph.Graph
import java.util.LinkedList
import java.util.Queue

class GraphVertexCycleSearchWithDfs : GraphVertexCycleSearch {
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
