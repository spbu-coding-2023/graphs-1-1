package display.minimumSpanningTree.implementation

import display.minimumSpanningTree.GraphMST
import graph.Graph
import graph.abstracts.AbstractGraph

/**
 * Works efficiently for graphs that already have a vertex map.
 * For those that don't, you're going to have to implement your own function :)
 */
class GraphMSTWithKruskal <V, E> : GraphMST<V, E> {
    override fun getMST(graph: Graph<V, E>) : Set<E>? {
        val mst = mutableSetOf<E>()
        if (graph.configuration.isDirected()) {
            return null
        }
        return if (graph.configuration.isUnweighted()) getMSTUnweighted(graph, mst) else getMSTWeighted(graph, mst)
    }

    private class DisjointSetUnion(numberOfVertices: Int) {
        private val parent = IntArray(numberOfVertices) { it }
        private val rank = IntArray(numberOfVertices) { 1 }

        fun find(a: Int) : Int {
            if (parent[a] != a) {
                parent[a] = find(parent[a])
            }
            return parent[a]
        }

        fun union(a: Int, b: Int) : Boolean {
            val rootA = find(a)
            val rootB = find(b)
            if (rootA == rootB) return false

            if (rank[rootA] > rank[rootB]) {
                parent[rootB] = rootA
            } else if (rank[rootA] < rank[rootB]) {
                parent[rootA] = rootB
            } else {
                parent[rootB] = rootA
                rank[rootA]++
            }
            return true
        }
    }

    private fun getMSTWeighted(graph: Graph<V, E>, mst: MutableSet<E>) : Set<E> {
        val disjointSetUnion = DisjointSetUnion(graph.getNOfVertices())
        val edges = graph.edgeSet().toList().sortedBy { graph.getEdgeWeight(it) }

        val verticesMap = graph.getVerticesMap()
        if (verticesMap.isEmpty()) return setOf()

        edges.forEach { edge ->
            val tail = graph.getEdgeTail(edge)
            val head = graph.getEdgeHead(edge)
            if (tail != null && head != null) {
                val tailIndex = verticesMap[tail]
                val headIndex = verticesMap[head]
                if (tailIndex != null && headIndex != null) {
                    if (disjointSetUnion.union(tailIndex, headIndex)) {
                        mst.add(edge)
                    }
                }
            }
        }
        return mst
    }

    private fun getMSTUnweighted(graph: Graph<V, E>, mst: MutableSet<E>) : Set<E> {
        val queue = ArrayDeque<V>()
        val hashSet = HashSet<V>()
        queue.addFirst(graph.vertexSet().first())
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            for (edge in graph.outgoingEdgesOf(current)) {
                val tail = graph.getEdgeTail(edge)
                val head = graph.getEdgeHead(edge)
                val neighbour = if (tail != current) tail else if (head != current) head else continue
                if (neighbour !in hashSet) {
                    hashSet.add(neighbour)
                    queue.addFirst(neighbour)
                    mst.add(edge)
                }
            }
        }
        return mst
    }
}

