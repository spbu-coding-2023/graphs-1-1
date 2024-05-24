package display.stronglyConnectedComponentSearch.implementation

import display.stronglyConnectedComponentSearch.GraphSCCSearch
import graph.Graph
import java.util.Stack
import kotlin.collections.HashMap
import kotlin.math.min

class GraphSCCSearchWithTarjan<V, E> : GraphSCCSearch<V, E> {
    override fun getSCCs(graph: Graph<V, E>) : Set<Set<V>>? {
        if (graph.configuration.isUndirected()) {
            return null
        }

        val indexMap = HashMap<V, Int>()
        val lowLinkMap = HashMap<V, Int>()
        val stack = Stack<V>()
        val onStack = HashSet<V>()
        val SCCs = mutableSetOf<Set<V>>()

        var index = 0
        for (v: V in graph.vertexSet()) {
            if (indexMap[v] == null) {
                index = strongConnect(graph, v, index, indexMap, lowLinkMap, stack, onStack, SCCs)
            }
        }
        return SCCs
    }

    private fun strongConnect(
        graph: Graph<V, E>, v: V, currentIndex: Int,
        indexMap: HashMap<V, Int>, lowLinkMap: HashMap<V, Int>,
        stack: Stack<V>, onStack: HashSet<V>,
        SCCs: MutableSet<Set<V>>
    ): Int {
        var index = currentIndex
        indexMap[v] = index
        lowLinkMap[v] = index
        index++
        stack.push(v)
        onStack.add(v)

        for (outgoingNeighbour in graph.outgoingVerticesOf(v)) {
            if (indexMap[outgoingNeighbour] == null) {
                index = strongConnect(graph, outgoingNeighbour, index, indexMap, lowLinkMap, stack, onStack, SCCs)
                lowLinkMap[v] = min(lowLinkMap[v]!!, lowLinkMap[outgoingNeighbour]!!)
            } else if (onStack.contains(outgoingNeighbour)) {
                lowLinkMap[v] = min(lowLinkMap[v]!!, indexMap[outgoingNeighbour]!!)
            }
        }

        if (lowLinkMap[v] == indexMap[v]) {
            val component = mutableSetOf<V>()
            var w: V
            do {
                w = stack.pop()
                onStack.remove(w)
                component.add(w)
            } while (w != v)
            SCCs.add(component)
        }

        return index
    }
}
