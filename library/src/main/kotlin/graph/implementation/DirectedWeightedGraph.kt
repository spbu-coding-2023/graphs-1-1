package graph.implementation

import graph.Graph

open class DirectedWeightedGraph<V, E> : Graph<V, E> {
    /**
     * store graph in list
     */
    private val graph = mutableMapOf<V, MutableSet<Pair<V, Edge<E>>>>()
    /**
     * Holds data and weight of an edge in the graph
     */
    internal data class Edge<T>(val data: T, var weight: Double)

    private val DEFAULT_EDGE_WEIGHT: Double = 1.0

    override fun addVertex(v: V): Boolean {
        if (v !in graph) {
            graph[v] = mutableSetOf()
            return true
        }
        return false
    }

    override fun addEdge(tail: V, head: V, e: E): Boolean {
        val destination = graph[tail]
        if (destination == null || graph[head] == null) throw IllegalArgumentException("To create an edge both vertecies must exist")
        if (destination.any { it.first == head }) return false // edge already exists

        destination.add(Pair(head, Edge(e, DEFAULT_EDGE_WEIGHT)))

        return true
    }

    override fun containsVertex(v: V): Boolean {
        return v in graph
    }

    override fun containsEdge(tail: V, head: V): Boolean {
        val destination = graph[tail] ?: return false
        return destination.any {it.first == head}
    }

    override fun degreeOf(v: V): Int {
        return (incomingEdgesOf(v) + outgoingEdgesOf(v)).size
    }

    override fun inDegreeOf(v: V): Int {
        return incomingEdgesOf(v).size
    }

    override fun outDegreeOf(v: V): Int {
        return outgoingEdgesOf(v).size
    }

    override fun edgeSet(): Set<E> {
        val edges = mutableSetOf<E>()
        graph.values.forEach {
            it.forEach { d -> edges.add(d.second.data) }
        }
        return edges
    }

    override fun vertexSet(): Set<V> {
        return graph.keys
    }

    override fun setEdgeWeight(e: E, w: Double) {
        graph.values.forEach {
            it.forEach { d -> if (d.second.data == e) d.second.weight = w }
        }
    }

    override fun removeVertex(v: V): Boolean {
        if (v !in graph) return false
        //        val destination = graph[v] ?: return false

        // clear edges from vertex
//        destination.clear()

        // clear edges to vertex
        for (k in graph.keys) {
            val u = graph[k] ?: continue
            graph[k] = u.filter { it.first != v }.toMutableSet()
        }

        // remove vertex
        graph.remove(v)
        return true
    }

    override fun removeEdge(tail: V, head: V): Boolean {
        val destination = graph[tail] ?: return false
        if (!destination.any { it.first == head }) return false
        graph[tail] = destination.filter { it.first != head }.toMutableSet()
        return true
    }

    override fun outgoingEdgesOf(v: V): Set<E> {
        val destination = graph[v] ?: return setOf()
        val edges = mutableSetOf<E>()
        destination.forEach { edges.add(it.second.data) }
        return edges
    }

    override fun incomingEdgesOf(v: V): Set<E> {
        if (v !in graph) return setOf()
        val edges = mutableSetOf<E>()
        graph.values.forEach { it.forEach { u -> if (u.first == v) edges.add(u.second.data) } }
        return edges
    }

    override fun getEdgeWeight(e: E): Double {
        graph.values.forEach {
            it.forEach { d -> if (d.second.data == e) return d.second.weight }
        }
        throw Error("Can not get edge weight: Edge does not exist")
    }

    override fun getEdge(tail: V, head: V): E? {
        val destination = graph[tail] ?: return null
        destination.forEach { if (it.first == head) return it.second.data }
        return null
    }

    override fun edgesOf(v: V): Set<E> {
        return incomingEdgesOf(v) + outgoingEdgesOf(v)
    }
}