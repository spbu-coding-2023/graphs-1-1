package display.placement

import graph.Graph

interface GraphPlacement {
    fun <V, E> getPlacement(graph: Graph<V, E>): Map<V, Pair<Float, Float>>
}