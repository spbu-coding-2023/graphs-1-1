package viewModel.workspace.graph

import androidx.lifecycle.ViewModel
import display.placement.GraphPlacement
import graph.Graph

class GraphViewModel<V, E>(
    val graph: Graph<V, E>,
    val graphPlacement: GraphPlacement
) : ViewModel() {
    private var placement = graphPlacement.getPlacement(graph)

    fun updatePlacement() {
        placement = graphPlacement.getPlacement(graph)
    }

    /**
     * Returns if edge is directed or not
     */
    fun isEdgesDirected(): Boolean {
        return graph.configuration.isDirected()
    }

    /**
     * Returns first point, second point and boolean value is directed (true if so, false otherwise)
     */
    fun getEdgesPlacement(): Set<Pair<Pair<Float, Float>, Pair<Float, Float>>> {
        val edgesPlacement = mutableSetOf<Pair<Pair<Float, Float>, Pair<Float, Float>>>()
        val isDirected = isEdgesDirected()
        for ((v, u) in graph.edgeSetOfVertecies()) {
            val vPos = placement[v] ?: throw IllegalStateException("Should not happen")
            val uPos = placement[u] ?: throw IllegalStateException("Should not happen")

            if (isDirected || Pair(uPos, vPos) !in edgesPlacement) {
                edgesPlacement.add(Pair(vPos, uPos))
            }
        }

        return edgesPlacement
    }

    fun getVerteciesPlacement(): Set<Pair<Float, Float>> {
        return placement.values.toSet()
    }
}