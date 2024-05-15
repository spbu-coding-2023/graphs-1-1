package graph.abstracts

import graph.Graph
import graph.configuration.GraphConfiguration
import graph.utils.AdjacencyMatrix

abstract class AbstractGraph<V, E>(isDirected: Boolean, isWeighted: Boolean) : Graph<V, E> {
    /**
     * type of the graph
     */
    override val configuration = GraphConfiguration(isDirected, isWeighted)

    /**
     * store graph in adjacency matrix
     */
    internal val structure = AdjacencyMatrix<V, Edge<E>>()

    /**
     * Holds data and weight of an edge in the graph
     */
    internal data class Edge<T>(val data: T, var weight: Double)

    /**
     * Initial edges are set to default weight, if not explicitly
     */
    protected val DEFAULT_EDGE_WEIGHT: Double = 1.0

    override fun addVertex(v: V): Boolean {
        return structure.add(v)
    }

    override fun addEdge(tail: V, head: V, e: E): Boolean {
        var edge = false
        if (configuration.isUndirected()) {
            edge = structure.set(tail, head, null)
        }
        return structure.set(tail, head, Edge(e, DEFAULT_EDGE_WEIGHT)) || edge
    }

    override fun containsVertex(v: V): Boolean {
        return v in structure.verteciesMap
    }

    override fun containsEdge(tail: V, head: V): Boolean {
        if (configuration.isDirected()) return containsEdgeDirected(tail, head)
        return containsEdgeDirected(tail, head) || containsEdgeDirected(head, tail)
    }

    private fun containsEdgeDirected(tail: V, head: V): Boolean {
        return structure.get(tail, head) != null
    }

    override fun degreeOf(v: V): Int {
        return (incomingEdgesOf(v) + outgoingEdgesOf(v)).size
    }

    override fun inDegreeOf(v: V): Int {
        if (configuration.isUndirected()) return degreeOf(v)
        return incomingEdgesOf(v).size
    }

    override fun outDegreeOf(v: V): Int {
        if (configuration.isUndirected()) return degreeOf(v)
        return outgoingEdgesOf(v).size
    }

    override fun edgeSet(): Set<E> {
        val edges = mutableSetOf<E>()
        structure.matrix.forEach {row ->
            row.forEach { edgeItem ->
                val edge = edgeItem?.data
                if (edge != null) edges.add(edge)
            }
        }
        return edges
    }

    override fun edgeSetOfVertecies(): Set<Pair<V, V>> {
        if (configuration.isDirected()) return edgeSetOfVerteciesDirected()
        return edgeSetOfVerteciesUndirected()
    }

    private fun edgeSetOfVerteciesDirected(): Set<Pair<V, V>> {
        val connections = mutableSetOf<Pair<V, V>>()
        val vertecies = vertexSet()
        for (v1 in vertecies) {
            for (v2 in vertecies) {
                if (structure.get(v1, v2) != null) connections.add(Pair(v1, v2))
            }
        }
        return connections
    }

    private fun edgeSetOfVerteciesUndirected(): Set<Pair<V, V>> {
        val connections = mutableSetOf<Pair<V, V>>()
        val vertecies = vertexSet()
        for (v1 in vertecies) {
            for (v2 in vertecies) {
                if (structure.get(v1, v2) != null) {
                    connections.add(Pair(v1, v2))
                    connections.add(Pair(v2, v1))
                }
            }
        }
        return connections
    }

    override fun vertexSet(): Set<V> {
        return structure.verteciesMap.keys
    }

    override fun getEdgeHead(e: E): V {
        structure.matrix.forEach { row -> row.forEachIndexed { index, edgeItem ->
            if (edgeItem?.data == e) {
                structure.verteciesMap.forEach { if (it.value == index) return it.key }
            }
        } }
        throw Error("Can not find edge head: Edge does not exist")
    }

    override fun getEdgeTail(e: E): V {
        structure.matrix.forEachIndexed { index, row -> row.forEach { edgeItem ->
            if (edgeItem?.data == e) {
                structure.verteciesMap.forEach { if (it.value == index) return it.key }
            }
        } }
        throw Error("Can not find edge tail: Edge does not exist")
    }

    override fun setEdgeWeight(e: E, w: Double) {
        if (configuration.isUnweighted()) return

        structure.matrix.forEach { row -> row.forEach { edgeItem ->
            val edge = edgeItem?.data
            if (edge == e) edgeItem?.weight = w
        } }
    }

    override fun removeVertex(v: V): Boolean {
        if (v !in structure.verteciesMap.keys) return false
        structure.delete(v)
        return true
    }

    override fun removeEdge(tail: V, head: V): Boolean {
        if (configuration.isDirected()) return removeEdgeDirected(tail, head)
        val e1 = removeEdgeDirected(tail, head)
        val e2 = removeEdgeDirected(head, tail)
        return e1 || e2
    }

    private fun removeEdgeDirected(tail: V, head: V): Boolean {
        return !structure.set(tail, head, null)
    }

    override fun outgoingEdgesOf(v: V): Set<E> {
        if (configuration.isUndirected()) return outgoingEdgesOfDirected(v) + incomingEdgesOfDirected(v)
        return outgoingEdgesOfDirected(v)
    }

    private fun outgoingEdgesOfDirected(v: V): Set<E> {
        val edges = mutableSetOf<E>()
        for (head in structure.verteciesMap.keys) {
            val edge = structure.get(v, head) ?: continue
            edges.add(edge.data)
        }
        return edges
    }

    override fun incomingEdgesOf(v: V): Set<E> {
        if (configuration.isUndirected()) return outgoingEdgesOfDirected(v) + incomingEdgesOfDirected(v)
        return incomingEdgesOfDirected(v)
    }

    private fun incomingEdgesOfDirected(v: V): Set<E> {
        val edges = mutableSetOf<E>()
        for (tail in structure.verteciesMap.keys) {
            val edge = structure.get(tail, v) ?: continue
            edges.add(edge.data)
        }
        return edges
    }

    override fun outgoingVerteciesOf(v: V): Set<V> {
        if (configuration.isUndirected()) return verteciesOfUndirected(v)
        return outgoingVerteciesOfDirected(v)
    }

    private fun outgoingVerteciesOfDirected(v: V): Set<V> {
        val vertecies = mutableSetOf<V>()
        for (head in structure.verteciesMap.keys) {
            if (structure.get(v, head) == null) continue
            vertecies.add(head)
        }
        return vertecies
    }

    private fun verteciesOfUndirected(v: V): Set<V> {
        val vertecies = mutableSetOf<V>()
        for (head in structure.verteciesMap.keys) {
            if (structure.get(v, head) == null && structure.get(head, v) == null) continue
            vertecies.add(head)
        }
        return vertecies
    }

    override fun incomingVerteciesOf(v: V): Set<V> {
        if (configuration.isUndirected()) return verteciesOfUndirected(v)
        return incomingVerteciesOfDirected(v)
    }

    private fun incomingVerteciesOfDirected(v: V): Set<V> {
        val vertecies = mutableSetOf<V>()
        for (tail in structure.verteciesMap.keys) {
            if (structure.get(tail, v) == null) continue
            vertecies.add(tail)
        }
        return vertecies
    }

    override fun getEdgeWeight(e: E): Double {
        if (configuration.isUnweighted()) return DEFAULT_EDGE_WEIGHT

        structure.matrix.forEach { row -> row.forEach { edgeItem ->
            if (edgeItem != null && edgeItem.data == e) return edgeItem.weight
        } }
        throw Error("Can not get edge weight: Edge does not exist")
    }

    override fun getEdge(tail: V, head: V): E? {
        if (configuration.isUndirected()) return structure.get(tail, head)?.data ?: structure.get(head, tail)?.data
        return structure.get(tail, head)?.data
    }

    override fun edgesOf(v: V): Set<E> {
        return incomingEdgesOf(v) + outgoingEdgesOf(v)
    }
}