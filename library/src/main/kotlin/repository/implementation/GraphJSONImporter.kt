package repository.implementation

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import graph.Graph
import repository.GraphImporter
import java.io.File

class GraphJSONImporter : GraphImporter {
    private data class GraphRepresentation<V, E>(
        @SerializedName("vertexList") val vertexList: MutableList<MutableList<Triple<V, E, Double>>>,
        @SerializedName("vertexListMap") val vertexListMap: MutableList<V>
    )
    override fun <V, E> importGraph(graph: Graph<V, E>, file: File) {
        val gson = Gson()
        val jsonGraph = file.readText()
        val graphRepresentation = gson.fromJson(jsonGraph, GraphRepresentation::class.java) as GraphRepresentation<V, E>
        println(jsonGraph)
        println(graphRepresentation)

        val vertexList = graphRepresentation.vertexList
        val vertexListMap = graphRepresentation.vertexListMap

        for (v1 in vertexListMap) {
            graph.addVertex(v1)
            val index = vertexListMap.indexOf(v1)
            vertexList[index].forEach { (v2, edge, edgeWeight) ->
                graph.addVertex(v2)
                graph.addEdge(v1, v2, edge)
                graph.setEdgeWeight(edge, edgeWeight)
            }
        }
    }
}