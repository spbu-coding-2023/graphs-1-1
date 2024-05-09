package graph.implementation

class UndirectedUnweightedGraph<V, E> : UndirectedWeightedGraph<V, E>() {
    override fun getEdgeWeight(e: E): Double {
        throw UnsupportedOperationException("Graph is unweighted")
    }

    override fun setEdgeWeight(e: E, w: Double) {
        throw UnsupportedOperationException("Graph is unweighted")
    }
}