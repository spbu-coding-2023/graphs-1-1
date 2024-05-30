package display.community.implementation

import display.community.GraphCommunity
import graph.Graph
import org.gephi.graph.api.Edge
import org.gephi.graph.api.GraphController
import org.gephi.graph.api.Node
import org.gephi.project.api.ProjectController
import org.gephi.statistics.plugin.Modularity
import org.openide.util.Lookup

class LouvainCommunity : GraphCommunity {
    override fun <V, E> getCommunities(graph: Graph<V, E>): Map<V, Int> {
        val pc = Lookup.getDefault().lookup(ProjectController::class.java)
        pc.newProject()

        val graphModel = Lookup.getDefault().lookup(GraphController::class.java).graphModel
        val graphType = graphModel.directedGraph

        val map = mutableMapOf<V, Node>()

        for (vertex in graph.vertexSet()) {
            val n: Node = graphModel.factory().newNode(vertex.toString())
            map[vertex] = n
            graphType.addNode(n)
        }

        for (edge in graph.edgeSet()) {
            val e: Edge = graphModel.factory().newEdge(
                map[graph.getEdgeTail(edge)],
                map[graph.getEdgeHead(edge)],
                1,
                false
            )
            graphType.addEdge(e)
        }
        val modularity = Modularity()
        modularity.execute(graphType)

        val communityMap = mutableMapOf<V, Int>()

        for ((vertex, node) in map) {
            val community = node.getAttribute(Modularity.MODULARITY_CLASS) as Int
            communityMap[vertex] = community
        }

        return communityMap
    }
}
