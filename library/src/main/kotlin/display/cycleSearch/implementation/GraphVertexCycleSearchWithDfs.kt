package display.cycleSearch.implementation

import display.cycleSearch.GraphVertexCycleSearch
import graph.Graph

class GraphVertexCycleSearchWithDfs : GraphVertexCycleSearch {
    override fun <V, E> getTrailCycles(graph: Graph<V, E>, startingVertex: V): List<List<V>> {
        val cycles = listOf<List<V>>()

        fun getTrailCyclesRec(currentVertex: V, currentTrail: List<V>) {
            currentTrail.addLast(currentVertex)

            for (edge in graph.outgoingEdgesOf(currentVertex)) {
                val neighbourVertex = graph.getEdgeTail(edge)

                if (
                    (neighbourVertex in currentTrail) &&
                    (neighbourVertex == startingVertex) &&
                    (currentTrail.size > 2)
                    ) {
                    // fond cycle
                    val fullCycle = currentTrail.toList()
                    fullCycle.addLast(startingVertex)
                    cycles.addLast(fullCycle)
                }

                getTrailCyclesRec(neighbourVertex, currentTrail.toList())

                currentTrail.removeLast()
            }
        }

        getTrailCyclesRec(startingVertex, listOf())
        return cycles
    }

    override fun <V, E> getPathCycles(graph: Graph<V, E>, startingVertex: V): List<List<V>> {
        val cycles = listOf<List<V>>()
        val visited = mutableMapOf<V, Boolean>()
        graph.vertexSet().forEach { visited[it] = false }

        fun getPathCyclesRec(currentVertex: V, currentVisited: MutableMap<V, Boolean>, currentPath: List<V>) {
            currentVisited[currentVertex] = true
            currentPath.addLast(currentVertex)

            for (edge in graph.outgoingEdgesOf(currentVertex)) {
                val neighbourVertex = graph.getEdgeTail(edge)

                if ((neighbourVertex == startingVertex) && (currentPath.size > 2)) {
                    // found cycle
                    val fullCycle = currentPath.toList()
                    fullCycle.addLast(neighbourVertex)
                    cycles.addLast(fullCycle)
                } else if (
                    !currentVisited[neighbourVertex]!! && // !!! :)
                    (neighbourVertex != startingVertex)
                    ) {
                    getPathCyclesRec(neighbourVertex, currentVisited, currentPath)
                }
            }

            currentPath.removeLast()
            currentVisited[currentVertex] = false
        }

        getPathCyclesRec(startingVertex, visited, listOf())
        return cycles
    }
}