package graph.implementation

open class UndirectedWeightedGraph<V, E> : DirectedWeightedGraph<V, E>() {
    override fun containsEdge(tail: V, head: V): Boolean {
        return super.containsEdge(tail, head) || super.containsEdge(head, tail)
    }

    override fun removeEdge(tail: V, head: V): Boolean {
        val isPresent = super.removeEdge(tail, head) || super.removeEdge(head, tail)
        return isPresent
    }

    override fun edgeSetOfVertecies(): Set<Pair<V, V>> {
        val connections = mutableSetOf<Pair<V, V>>()
        val vertecies = vertexSet()
        for (v1 in vertecies) {
            for (v2 in vertecies) {
                if (graph.get(v1, v2) != null) {
                    connections.add(Pair(v1, v2))
                    connections.add(Pair(v2, v1))
                }
            }
        }
        return connections
    }

    override fun inDegreeOf(v: V): Int = degreeOf(v)

    override fun outDegreeOf(v: V): Int = degreeOf(v)
}