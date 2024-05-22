package graph

import org.gephi.graph.impl.GraphStoreConfiguration.DEFAULT_EDGE_WEIGHT

import graph.configuration.GraphConfiguration

/**
 * Common interface for all graphs
 */
interface Graph<V, E> {

    /**
     * Adds vertex to the graph if not present
     */
    fun addVertex(v: V): Boolean

    /**
     * Adds Edge between tail and head to the graph if not present
     */
    fun addEdge(tail: V, head: V, e: E, weight : Double = DEFAULT_EDGE_WEIGHT): Boolean

    /**
     * Returns true if vertex is in the graph
     */
    fun containsVertex(v: V): Boolean

    /**
     * Returns true if edge between tail and head is in the graph
     */
    fun containsEdge(tail: V, head: V): Boolean

    /**
     * Returns the degree of a vertex
     */
    fun degreeOf(v: V): Int

    /**
     * Returns the in-degree of a vertex
     */
    fun inDegreeOf(v: V): Int

    /**
     * Returns the out-degree of a vertex
     */
    fun outDegreeOf(v: V): Int

    /**
     * Returns set of all edges in graph
     */
    fun edgeSet(): Set<E>

    /**
     * Returns set of all connections of each edge
     */
    fun edgeSetOfVertices(): Set<Pair<V, V>>

    /**
     * Returns set of all edges connected to a vertex in graph (any direction)
     */
    fun edgesOf(v: V): Set<E>

    /**
     * Returns edge between vertex tail and vertex head if exists, otherwise null
     */
    fun getEdge(tail: V, head: V): E?

    /**
     * Returns weight of an edge
     */
    fun getEdgeWeight(e: E): Double
    fun getEdgeWeight(tail: V, head: V): Double

    /**
     * Returns all incoming edges in vertex
     */
    fun incomingEdgesOf(v: V): Set<E>

    /**
     * Returns all outgoing edges in vertex
     */
    fun outgoingEdgesOf(v: V): Set<E>

    /**
     * Returns all incoming vertices of a vertex
     */
    fun incomingVerticesOf(v: V): Set<V>

    /**
     * Returns all outgoing vertices of a vertex
     */
    fun outgoingVerticesOf(v: V): Set<V>

    /**
     * Removes edge and returns true if edge was present in the graph
     */
    fun removeEdge(tail: V, head: V): Boolean

    /**
     * Removes vertex and all its touching edges
     */
    fun removeVertex(v: V): Boolean

    /**
     * Sets weight to an edge
     */
    fun setEdgeWeight(e: E, w: Double)
    fun setEdgeWeight(tail: V, head: V, w: Double)

    /**
     * Returns set of all vertices in the graph
     */
    fun vertexSet(): Set<V>

    /**
     * Returns start vertex of an edge
     */
    fun getEdgeTail(e: E): V

    /**
     * Returns end vertex of an edge
     */
    fun getEdgeHead(e: E): V

    /**
     * type of graph, how it is configured
     */
    val configuration: GraphConfiguration
}
