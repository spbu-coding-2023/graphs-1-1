package graph.implementation

open class UndirectedWeightedGraph<V, E> : DirectedWeightedGraph<V, E>() {
    override fun addEdge(tail: V, head: V, e: E): Boolean {
        val isPresent = super.addEdge(tail, head, e) || super.addEdge(head, tail, e)
        return isPresent
    }

    override fun containsEdge(tail: V, head: V): Boolean {
        return super.containsEdge(tail, head) || super.containsEdge(head, tail)
    }

    override fun removeEdge(tail: V, head: V): Boolean {
        val isPresent = super.removeEdge(tail, head) || super.removeEdge(head, tail)
        return isPresent
    }
}