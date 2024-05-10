package graph.implementation

open class DirectedUnweightedGraph<V, E> : DirectedWeightedGraph<V, E>() {
    override fun getEdgeWeight(e: E): Double {
        return DEFAULT_EDGE_WEIGHT
    }

    override fun setEdgeWeight(e: E, w: Double) {
        return
    }
}