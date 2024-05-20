package repository.implementation

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import graph.Graph
import repository.GraphExporter
import java.io.File

class GraphJSONExporter : GraphExporter {
    private data class GraphRepresentation<V, E>(
        val vertexList: MutableList<MutableList<Triple<V, E, Double>>>,
        val vertexListMap: MutableList<V>,
        val graphTypeWeighted: Boolean,
        val graphTypeDirected: Boolean
    )
    override fun <V, E> exportGraph(graph: Graph<V, E>, file: File) {
        val ow = jacksonObjectMapper().writer().withDefaultPrettyPrinter()
        val graphRepresentation = GraphRepresentation(
            mutableListOf<MutableList<Triple<V, E, Double>>>(),
            mutableListOf(),
            graph.configuration.isWeighted(),
            graph.configuration.isDirected()
            )

        val vertexList = graphRepresentation.vertexList
        val vertexListMap = graphRepresentation.vertexListMap

        for (v in graph.vertexSet()) {
            vertexListMap.add(v)
        }
        repeat(vertexListMap.size) {
            vertexList.add(mutableListOf())
        }

        graph.edgeSetOfVerticesDirectional().forEach { (v1, v2) ->
            val edge = graph.getEdge(v1, v2)!!
            val edgeWeight = graph.getEdgeWeight(edge)

            val index = vertexListMap.indexOf(v1)
            vertexList[index].add(Triple(v2, edge, edgeWeight))
        }

        val jsonGraph = ow.writeValueAsString(graphRepresentation)

        file.writeText(jsonGraph)
    }
}
