package display.keyVertex.implementation


import display.keyVertex.GraphKeyVertex
import graph.Graph
import java.util.*


class GraphBetweennessCentrality : GraphKeyVertex {
    override fun <V, E> getKeyVertices(graph: Graph<V, E>): Map<V, Float> {
        val betweennessMap = mutableMapOf<V, Float>().withDefault { 0f }
        val vertices = graph.vertexSet()
        for (s in vertices) {
            val stack = Stack<V>()
            val predecessors = mutableMapOf<V, MutableList<V>>().withDefault { mutableListOf() }
            val sigma = mutableMapOf<V, Float>().withDefault { 0f }
            val distance = mutableMapOf<V, Int>().withDefault { -1 }
            val delta = mutableMapOf<V, Float>().withDefault { 0f }

            sigma[s] = 1f
            distance[s] = 0

            val queue: Queue<V> = LinkedList<V>()
            queue.add(s)

            while (queue.isNotEmpty()) {
                val v = queue.remove()
                stack.push(v)
                val incoming = graph.incomingVerticesOf(v)
                val outgoing = graph.outgoingVerticesOf(v)
                for (w in incoming.union(outgoing)) {
                    if (distance[w] == -1) {
                        queue.add(w)
                        distance[w] = distance[v]!! + 1
                    }
                    if (distance[w] == distance[v]!! + 1) {
                        sigma[w] = sigma[w]!! + sigma[v]!!
                        predecessors[w]!!.add(v)
                    }
                }
            }

            while (stack.isNotEmpty()) {
                val w = stack.pop()
                for (v in predecessors[w]!!) {
                    delta[v] = delta[v]!! + (sigma[v]!! / sigma[w]!!) * (1 + delta[w]!!)
                }
                if (w != s) {
                    betweennessMap[w] = betweennessMap[w]!! + delta[w]!!
                }
            }
        }

        return betweennessMap
    }
}
