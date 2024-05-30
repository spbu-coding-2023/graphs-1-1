package graph.configuration

class GraphConfiguration(
    private var isDirected: Boolean,
    private var isWeighted: Boolean
) {
    fun asWeighted() { isWeighted = true }
    fun asUnweighted() { isWeighted = false }
    fun asDirected() { isDirected = true }
    fun asUndirected() { isDirected = false }

    fun isWeighted(): Boolean = isWeighted
    fun isUnweighted(): Boolean = !isWeighted
    fun isDirected(): Boolean = isDirected
    fun isUndirected(): Boolean = !isDirected
}
