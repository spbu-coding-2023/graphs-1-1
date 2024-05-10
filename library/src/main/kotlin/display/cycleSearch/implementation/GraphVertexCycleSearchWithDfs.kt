package display.cycleSearch.implementation

import display.cycleSearch.GraphVertexCycleSearch
import graph.Graph

class GraphVertexCycleSearchWithDfs : GraphVertexCycleSearch {
    override fun <V, E> getTrailCycles(graph: Graph<V, E>, startingVertex: V): List<List<V>> {
        val cycles = mutableListOf<List<V>>()

        fun getTrailCyclesRec(currentVertex: V, currentTrail: MutableList<V>) {
            currentTrail.add(currentVertex)

            for (edge in graph.outgoingEdgesOf(currentVertex)) {
                val neighbourVertex = graph.getEdgeTail(edge)

                if (
                    (neighbourVertex in currentTrail) &&
                    (neighbourVertex == startingVertex) &&
                    (currentTrail.size > 2)
                    ) {
                    // fond cycle
                    val fullCycle = currentTrail.toMutableList()
                    fullCycle.add(startingVertex)
                    cycles.add(fullCycle)
                }

                getTrailCyclesRec(neighbourVertex, currentTrail.toMutableList())

                currentTrail.removeLast()
            }
        }

        getTrailCyclesRec(startingVertex, mutableListOf())
        return cycles
    }

    override fun <V, E> getPathCycles(graph: Graph<V, E>, startingVertex: V): List<List<V>> {
        val cycles = mutableListOf<List<V>>()
        val visited = mutableMapOf<V, Boolean>()
        graph.vertexSet().forEach { visited[it] = false }

        fun getPathCyclesRec(currentVertex: V, currentVisited: MutableMap<V, Boolean>, currentPath: MutableList<V>) {
            currentVisited[currentVertex] = true
            currentPath.add(currentVertex)

            for (edge in graph.outgoingEdgesOf(currentVertex)) {
                val neighbourVertex = graph.getEdgeTail(edge)

                if ((neighbourVertex == startingVertex) && (currentPath.size > 2)) {
                    // found cycle
                    val fullCycle = currentPath.toMutableList()
                    fullCycle.add(neighbourVertex)
                    cycles.add(fullCycle)
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

        getPathCyclesRec(startingVertex, visited, mutableListOf())
        return cycles
    }
}