package graph.abstracts

import graph.Graph
import graph.configuration.GraphConfiguration

abstract class AbstractGraph<V, E>(isDirected: Boolean, isWeighted: Boolean) : Graph<V, E> {
    /**
     * number of Edges, Vertices
     */
    var numberOfEdges: Int = 0
    var numberOfVertices: Int = 0
    /**
     * type of the graph
     */
    val configuration = GraphConfiguration(isDirected, isWeighted)

    /**
     * store graph in adjacency matrix
     */
    protected val graph = AdjacencyMatrix<V, Edge<E>>()

    /**
     * adjacency matrix
     */
    protected inner class AdjacencyMatrix<V, E> {
        val matrix = mutableListOf<MutableList<E?>>()
        val verticesMap = HashMap<V, Int>()
        fun get(tail: V, head: V): E? {
            val tailIndex = verticesMap[tail]
            val headIndex = verticesMap[head]
            if (tailIndex == null || headIndex == null) throw IllegalArgumentException("Can not get edge by vertices: Both vertices must exist")
            return matrix[tailIndex][headIndex]
        }

        fun set(tail: V, head: V, e: E?): Boolean {
            val tailIndex = verticesMap[tail]
            val headIndex = verticesMap[head]
            if (tailIndex == null || headIndex == null) throw IllegalArgumentException("Can not set edge by vertices: Both vertices must exist")
            val prev = matrix[tailIndex][headIndex]
            matrix[tailIndex][headIndex] = e
            if (prev == null) {
                if (e != null) {
                    numberOfEdges++
                }
                return true
            }
            if (e == null) {
                numberOfEdges--
            }
            return false
        }

        fun add(v: V): Boolean {
            if (v in verticesMap) return false

            val matrixSize = matrix.size
            verticesMap[v] = matrixSize

            matrix.forEach {// adds by one line (could double each time, would be faster ig)
                it.add(null)
            }
            matrix.add(MutableList(matrixSize+1) {null})
            numberOfVertices++
            return true
        }

        fun delete(v: V) {
            val vertexIndex = verticesMap[v] ?: throw IllegalArgumentException("Can not delete vertex: vertex does not exist")

            for (i in matrix.indices) {
                if (matrix[vertexIndex][i] != null) {
                    numberOfEdges--
                }
                if (matrix[i][vertexIndex] != null) {
                    numberOfEdges--
                }
                matrix[i].removeAt(vertexIndex)
            }

            matrix.removeAt(vertexIndex)

            verticesMap.remove(v)

            numberOfVertices--

            verticesMap.forEach {
                if (it.value > vertexIndex) {
                    verticesMap[it.key] = it.value - 1
                }
            }
        }
    }

    /**
     * Holds data and weight of an edge in the graph
     */
    protected data class Edge<T>(val data: T, var weight: Double)

    /**
     * Initial edges are set to default weight, if not explicitly
     */
    protected val DEFAULT_EDGE_WEIGHT: Double = 1.0

    override fun addVertex(v: V): Boolean {
        return graph.add(v)
    }

    override fun addEdge(tail: V, head: V, e: E, weight : Double): Boolean {
        return graph.set(tail, head, Edge(e, weight))
    }

    override fun containsVertex(v: V): Boolean {
        return v in graph.verticesMap
    }

    override fun containsEdge(tail: V, head: V): Boolean {
        if (configuration.isDirected()) return containsEdgeDirected(tail, head)
        return containsEdgeDirected(tail, head) || containsEdgeDirected(head, tail)
    }

    private fun containsEdgeDirected(tail: V, head: V): Boolean {
        return graph.get(tail, head) != null
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
        graph.matrix.forEach {row ->
            row.forEach { edgeItem ->
                val edge = edgeItem?.data
                if (edge != null) edges.add(edge)
            }
        }
        return edges
    }

    override fun edgeSetOfVertices(): Set<Pair<V, V>> {
        if (configuration.isDirected()) return edgeSetOfVerticesDirected()
        return edgeSetOfVerticesUndirected()
    }

    private fun edgeSetOfVerticesDirected(): Set<Pair<V, V>> {
        val connections = mutableSetOf<Pair<V, V>>()
        val vertices = vertexSet()
        for (v1 in vertices) {
            for (v2 in vertices) {
                if (graph.get(v1, v2) != null) connections.add(Pair(v1, v2))
            }
        }
        return connections
    }

    private fun edgeSetOfVerticesUndirected(): Set<Pair<V, V>> {
        val connections = mutableSetOf<Pair<V, V>>()
        val vertices = vertexSet()
        for (v1 in vertices) {
            for (v2 in vertices) {
                if (graph.get(v1, v2) != null) {
                    connections.add(Pair(v1, v2))
                    connections.add(Pair(v2, v1))
                }
            }
        }
        return connections
    }

    override fun vertexSet(): Set<V> {
        return graph.verticesMap.keys
    }

    override fun getEdgeHead(e: E): V {
        graph.matrix.forEach { row -> row.forEachIndexed { index, edgeItem ->
            if (edgeItem?.data == e) {
                graph.verticesMap.forEach { if (it.value == index) return it.key }
            }
        } }
        throw Error("Can not find edge head: Edge does not exist")
    }

    override fun getEdgeTail(e: E): V {
        graph.matrix.forEachIndexed { index, row -> row.forEach { edgeItem ->
            if (edgeItem?.data == e) {
                graph.verticesMap.forEach { if (it.value == index) return it.key }
            }
        } }
        throw Error("Can not find edge tail: Edge does not exist")
    }

    override fun setEdgeWeight(e: E, w: Double) {
        if (configuration.isUnweighted()) return

        graph.matrix.forEach { row -> row.forEach { edgeItem ->
            val edge = edgeItem?.data
            if (edge == e) edgeItem?.weight = w
        } }
    }

    override fun removeVertex(v: V): Boolean {
        if (v !in graph.verticesMap.keys) return false
        graph.delete(v)
        return true
    }

    override fun removeEdge(tail: V, head: V): Boolean {
        if (configuration.isDirected()) return removeEdgeDirected(tail, head)
        val e1 = removeEdgeDirected(tail, head)
        val e2 = removeEdgeDirected(head, tail)
        return e1 || e2
    }

    private fun removeEdgeDirected(tail: V, head: V): Boolean {
        return !graph.set(tail, head, null)
    }

    override fun outgoingEdgesOf(v: V): Set<E> {
        if (configuration.isUndirected()) return outgoingEdgesOfDirected(v) + incomingEdgesOfDirected(v)
        return outgoingEdgesOfDirected(v)
    }

    private fun outgoingEdgesOfDirected(v: V): Set<E> {
        val edges = mutableSetOf<E>()
        for (head in graph.verticesMap.keys) {
            val edge = graph.get(v, head) ?: continue
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
        for (tail in graph.verticesMap.keys) {
            val edge = graph.get(tail, v) ?: continue
            edges.add(edge.data)
        }
        return edges
    }

    override fun outgoingVerticesOf(v: V): Set<V> {
        if (configuration.isUndirected()) return outgoingVerticesOfDirected(v) + incomingVerticesOfDirected(v)
        return outgoingVerticesOfDirected(v)
    }

    private fun outgoingVerticesOfDirected(v: V): Set<V> {
        val vertices = mutableSetOf<V>()
        for (head in graph.verticesMap.keys) {
            if (graph.get(v, head) == null) continue
            vertices.add(head)
        }
        return vertices
    }

    override fun incomingVerticesOf(v: V): Set<V> {
        if (configuration.isUndirected()) return outgoingVerticesOfDirected(v) + incomingVerticesOfDirected(v)
        return incomingVerticesOfDirected(v)
    }

    private fun incomingVerticesOfDirected(v: V): Set<V> {
        val vertices = mutableSetOf<V>()
        for (tail in graph.verticesMap.keys) {
            if (graph.get(tail, v) == null) continue
            vertices.add(tail)
        }
        return vertices
    }

    override fun getEdgeWeight(e: E): Double {
        if (configuration.isUnweighted()) return DEFAULT_EDGE_WEIGHT

        graph.matrix.forEach { row -> row.forEach { edgeItem ->
            if (edgeItem != null && edgeItem.data == e) return edgeItem.weight
        } }
        throw Error("Can not get edge weight: Edge does not exist")
    }

    override fun getEdge(tail: V, head: V): E? {
        if (configuration.isUndirected()) return graph.get(tail, head)?.data ?: graph.get(head, tail)?.data
        return graph.get(tail, head)?.data
    }

    override fun edgesOf(v: V): Set<E> {
        return incomingEdgesOf(v) + outgoingEdgesOf(v)
    }

    override fun getNOfEdges(): Int {
        return numberOfEdges
    }

    override fun getNOfVertices(): Int {
        return numberOfVertices
    }

    override fun hasVerticesMap(): Boolean {
        return true
    }

    override fun getVerticesMap(): HashMap<V, Int> {
        if (hasVerticesMap()) {
            return graph.verticesMap
        } else {
            val verticesMap = HashMap<V, Int>()
            for ((index, vertex) in vertexSet().withIndex()) {
                verticesMap[vertex] = index
            }
            return verticesMap
        }
    }
}