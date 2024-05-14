package display.placement.implementation

import display.placement.GraphPlacement
import graph.Graph

import org.gephi.graph.api.Edge
import org.gephi.graph.api.GraphController
import org.gephi.graph.api.Node
import org.gephi.layout.plugin.force.StepDisplacement
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout
import org.gephi.project.api.ProjectController
import org.openide.util.Lookup

import kotlin.random.Random

class GraphPlacementYifanHu : GraphPlacement {
    private val _width = 1
    private val _height = 1
    override fun <V, E>getPlacement(graph: Graph<V, E>): Map<V, Pair<Float, Float>> {
        val random = Random(1L)

        val pc = Lookup.getDefault().lookup(ProjectController::class.java)
        pc.newProject()

        val graphModel = Lookup.getDefault().lookup(GraphController::class.java).graphModel
        val graphType = graphModel.undirectedGraph

        val map = mutableMapOf<V, Node>()
        val placement = mutableMapOf<V, Pair<Float, Float>>()

        for (vert in graph.vertexSet()) {
            val n: Node = graphModel.factory().newNode(vert.toString())
            n.setX(random.nextFloat()*10)
            n.setY(random.nextFloat()*10)
            map[vert] = n

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

        // Run YifanHuLayout for 100 passes
        val layout = YifanHuLayout(null, StepDisplacement(1f))
        layout.setGraphModel(graphModel)
        layout.initAlgo()
        layout.resetPropertiesValues()
        layout.optimalDistance = 100f
        layout.relativeStrength = 0.2f
        layout.initialStep = 20.0f
        layout.stepRatio = 0.95f
        layout.isAdaptiveCooling = true

        var i = 0
        while (i < 1000 && layout.canAlgo()) {
            layout.goAlgo()
            i++
        }
        layout.endAlgo()

        for (vertex in graph.vertexSet()) {
            val n: Node = graphType.getNode(vertex.toString())
            val x = ((_width/2 + n.x()* 3 / 1))
            val y = ((_height/2 + n.y()* 3 / 1))
            placement[vertex] = Pair(x, y)
        }

        return placement
    }
}
