package graph.implementation

open class UndirectedUnweightedGraph<V, E> : UndirectedWeightedGraph<V, E>() {
    override fun getEdgeWeight(e: E): Double {
        return DEFAULT_EDGE_WEIGHT
    }

    override fun setEdgeWeight(e: E, w: Double) {
        return
    }
}