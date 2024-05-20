package display.stronglyConnectedComponentSearch.implementation

import display.stronglyConnectedComponentSearch.GraphSCCSearch
import graph.Graph
import java.util.Stack
import kotlin.collections.HashMap
import kotlin.math.min


class GraphSCCSearch<V, E> {
    private var index = 0
    private val indexMap = HashMap<V, Int>()
    private val lowLinkMap = HashMap<V, Int>()
    private val stack = Stack<V>()
    private val onStack = HashMap<V, Boolean>()
    private val SCCs = mutableListOf<List<V>>()
    fun getSCCs(graph: Graph<V, E>) : List<List<V>> {
        for (v : V in graph.vertexSet()) {
            if (indexMap[v] == null) {
                strongConnect(graph, v)
            }
        }
        return SCCs
    }

    private fun strongConnect(graph: Graph<V, E>, v : V) {
        indexMap[v] = index
        lowLinkMap[v] = index
        index++
        stack.push(v)
        onStack[v] = true
        for (outgoingNeighbour in graph.outgoingVerticesOf(v)) {
            if (indexMap[outgoingNeighbour] == null) {
                strongConnect(graph, outgoingNeighbour)
                lowLinkMap[v] = min(lowLinkMap[v] ?: index, lowLinkMap[outgoingNeighbour] ?: index)
            } else if (onStack[outgoingNeighbour] == true) {
                lowLinkMap[v] = min(lowLinkMap[v] ?: index, indexMap[outgoingNeighbour] ?: index)
            }
        }
        if (lowLinkMap[v] == indexMap[v]) {
            val component = mutableListOf<V>()
            var w : V
            do {
                w = stack.pop()
                onStack[w] = false
                component.add(w)
            } while (w != v)
            SCCs.add(component)
        }
    }
}