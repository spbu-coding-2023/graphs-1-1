package display.placement.implementation

import display.placement.GraphPlacement
import graph.Graph
import kotlin.random.Random

class GraphPlacementRandom : GraphPlacement {
    private var _width = 100
    private var _height = 100

    fun setSize(w: Int, h: Int) {
        _width = w
        _height = h
    }

    override fun <V, E> getPlacement(graph: Graph<V, E>): Map<V, Pair<Float, Float>> {
        val placement = mutableMapOf<V, Pair<Float, Float>>()

        graph.vertexSet().forEach { v ->
            val rx = Random.nextFloat() * _width - _width/2
            val ry = Random.nextFloat() * _height - _height/2
            val randomPosition = Pair(rx, ry)
            placement[v] = randomPosition
        }

        return placement
    }
}