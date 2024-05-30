package repository.implementation.json

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import graph.Graph
import repository.GraphImporter
import java.io.File

class GraphJSONImporter : GraphImporter {
    private data class GraphRepresentation<V, E>(
        val vertexList: MutableList<MutableList<Triple<V, E, Double>>>,
        val vertexListMap: MutableList<V>,
        val graphTypeWeighted: Boolean,
        val graphTypeDirected: Boolean
    )

    fun <V, E>importGraph(graph: Graph<V, E>, file: File, vertexClass: Class<V>, edgeClass: Class<E>) {
        val mapper = jacksonObjectMapper()
        val jsonGraph = file.readText()

        val typeFactory = mapper.typeFactory
        val graphRepresentationType = typeFactory.constructParametricType(GraphRepresentation::class.java, vertexClass, edgeClass)
        val graphRepresentation = mapper.readValue<GraphRepresentation<V, E>>(jsonGraph, graphRepresentationType)

        graph.configuration.asDirected()
        graph.configuration.asWeighted()

        val vertexList = graphRepresentation.vertexList
        val vertexListMap = graphRepresentation.vertexListMap

        for (v1 in vertexListMap) {
            graph.addVertex(v1)
            val index = vertexListMap.indexOf(v1)
            vertexList[index].forEach { (v2, edge, edgeWeight) ->
                graph.addVertex(v2)
                graph.addEdge(v1, v2, edge, edgeWeight)
            }
        }

        if (graphRepresentation.graphTypeDirected) graph.configuration.asDirected()
        else graph.configuration.asUndirected()
        if (graphRepresentation.graphTypeWeighted) graph.configuration.asWeighted()
        else graph.configuration.asUnweighted()
    }

    override fun <V, E> importGraph(graph: Graph<V, E>, file: File) {
        val mapper = jacksonObjectMapper()
        val jsonGraph = file.readText()

        val graphRepresentation = mapper.readValue<GraphRepresentation<V, E>>(jsonGraph)

        graph.configuration.asDirected()
        graph.configuration.asWeighted()

        val vertexList = graphRepresentation.vertexList
        val vertexListMap = graphRepresentation.vertexListMap

        for (v1 in vertexListMap) {
            graph.addVertex(v1)
            val index = vertexListMap.indexOf(v1)
            vertexList[index].forEach { (v2, edge, edgeWeight) ->
                graph.addVertex(v2)
                graph.addEdge(v1, v2, edge, edgeWeight)
            }
        }

        if (graphRepresentation.graphTypeDirected) graph.configuration.asDirected()
        else graph.configuration.asUndirected()
        if (graphRepresentation.graphTypeWeighted) graph.configuration.asWeighted()
        else graph.configuration.asUnweighted()
    }
}
