package graph

/**
 * Common interface for all graphs
 */
interface Graph<V, E> {

    /**
     * Adds vertex to the graph if not present
     */
    fun addVertex(v: V): Boolean
    // fun addVertex(): V

    /**
     * Adds Edge between tail and head to the graph if not present
     */
    fun addEdge(tail: V, head: V, e: E): Boolean
    // fun addEdge(tail: V, head: V): E

    /**
     * Returns true if vertex is in the graph
     */
    fun containsVertex(v: V): Boolean

    /**
     * Returns true if edge between tail and head is in the graph
     */
    fun containsEdge(tail: V, head: V): Boolean
    // fun containsEdge(e: E): Boolean

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
    fun edgeSetOfVertecies(): Set<Pair<V, V>>

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

    /**
     * Returns all incoming edges in vertex
     */
    fun incomingEdgesOf(v: V): Set<E>

    /**
     * Returns all outgoing edges in vertex
     */
    fun outgoingEdgesOf(v: V): Set<E>

    /**
     * Removes edge and returns true if edge was present in the graph
     */
//    fun removeEdge(e: E): Boolean
    fun removeEdge(tail: V, head: V): Boolean

    /**
     * Removes vertex and all its touching edges
     */
    fun removeVertex(v: V): Boolean

    /**
     * Sets weight to an edge
     */
    fun setEdgeWeight(e: E, w: Double)
    // fun setEdgeWeight(tail: V, head: V, w: Double)

    /**
     * Returns set of all vertecies in the graph
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
}
