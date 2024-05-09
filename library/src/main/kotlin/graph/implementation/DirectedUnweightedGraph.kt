package graph.implementation

open class DirectedUnweightedGraph<V, E> : DirectedWeightedGraph<V, E>() {
    override fun getEdgeWeight(e: E): Double {
        throw UnsupportedOperationException("Graph is unweighted")
    }

    override fun setEdgeWeight(e: E, w: Double) {
        throw UnsupportedOperationException("Graph is unweighted")
    }
}