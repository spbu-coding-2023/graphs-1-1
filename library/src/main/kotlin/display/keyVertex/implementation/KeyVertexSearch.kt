package display.keyVertex.implementation


import display.keyVertex.GraphKeyVertex
import graph.Graph
import java.util.*


class GraphBetweennessCentrality : GraphKeyVertex {
    fun <V, E> successors(graph: Graph<V, E>, v: V): Set<V> {
        val incoming = graph.incomingVerteciesOf(v)
        val outgoing = graph.outgoingVerteciesOf(v)
        return incoming.union(outgoing)
    }

    override fun <V, E> getKeyVertecies(graph: Graph<V, E>): Map<V, Float> {
        val betweennessMap = mutableMapOf<V, Float>().withDefault { 0f }
        val vertices = graph.vertexSet()
        for (sourceVertex in vertices) {
            val stack = Stack<V>()
            val predecessors = mutableMapOf<V, MutableList<V>>().withDefault { mutableListOf() }
            val sourceVertexWeight = mutableMapOf<V, Float>().withDefault { 0f }
            val distance = mutableMapOf<V, Int>().withDefault { -1 }
            val sourceVertexDependency = mutableMapOf<V, Float>().withDefault { 0f }

            sourceVertexWeight[sourceVertex] = 1f
            distance[sourceVertex] = 0

            val queue: Queue<V> = LinkedList<V>()
            queue.add(sourceVertex)

            while (queue.isNotEmpty()) {
                val currentVertex = queue.remove()
                stack.push(currentVertex)

                for (successorVertex in successors(graph, currentVertex)) {
                    if (distance[successorVertex] == -1) {
                        queue.add(successorVertex)
                        distance[successorVertex] = distance[currentVertex]!! + 1
                    }
                    if (distance[successorVertex] == distance[currentVertex]!! + 1) {
                        sourceVertexWeight[successorVertex] = sourceVertexWeight[successorVertex]!! + sourceVertexWeight[currentVertex]!!
                        predecessors[successorVertex]!!.add(currentVertex)
                    }
                }
            }

            while (stack.isNotEmpty()) {
                val successorVertex = stack.pop()
                for (currentVertex in predecessors[successorVertex]!!) {
                    sourceVertexDependency[currentVertex] = sourceVertexDependency[currentVertex]!! + (sourceVertexWeight[currentVertex]!! / sourceVertexWeight[successorVertex]!!) * (1 + sourceVertexDependency[successorVertex]!!)
                }
                if (successorVertex != sourceVertex) {
                    betweennessMap[successorVertex] = betweennessMap[successorVertex]!! + sourceVertexDependency[successorVertex]!!
                }
            }
        }

        return betweennessMap
    }
}
