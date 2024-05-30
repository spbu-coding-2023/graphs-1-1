package display.stronglyConnectedComponentSearch.implementation

import display.stronglyConnectedComponentSearch.GraphSCCSearch
import graph.Graph
import java.util.Stack
import kotlin.collections.HashMap
import kotlin.math.min


class GraphSCCSearchWithTarjan<V, E> : GraphSCCSearch<V, E> {
    private var index = 0
    private val indexMap = HashMap<V, Int>()
    private val lowLinkMap = HashMap<V, Int>()
    private val stack = Stack<V>()
    private val onStack = HashSet<V>()
    private val SCCs = mutableSetOf<Set<V>>()
    override fun getSCCs(graph: Graph<V, E>) : Set<Set<V>>? {
        if (graph.configuration.isUndirected()) {
            return null
        }
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
        onStack.add(v)
        for (outgoingNeighbour in graph.outgoingVerticesOf(v)) {
            if (indexMap[outgoingNeighbour] == null) {
                strongConnect(graph, outgoingNeighbour)
                lowLinkMap[v] = min(lowLinkMap[v]!!, lowLinkMap[outgoingNeighbour]!!)
            } else if (onStack.contains(outgoingNeighbour)) {
                lowLinkMap[v] = min(lowLinkMap[v]!!, indexMap[outgoingNeighbour]!!)
            }
        }
        if (lowLinkMap[v] == indexMap[v]) {
            val component = mutableSetOf<V>()
            var w : V
            do {
                w = stack.pop()
                onStack.remove(w)
                component.add(w)
            } while (w != v)
            SCCs.add(component)
        }
    }
}