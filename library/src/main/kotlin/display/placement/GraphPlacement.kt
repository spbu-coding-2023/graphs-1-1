package display.placement

import graph.Graph

interface GraphPlacement<V, E> {
    fun getPlacement(g: Graph<V, E>): Map<V, Pair<Float, Float>>
}