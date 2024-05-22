package repository.implementation.json

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import graph.Graph
import repository.GraphExporter
import java.io.File

class GraphJSONExporter : GraphExporter {
    private data class GraphRepresentation<V, E>(
        @SerializedName("vertexList") val vertexList: MutableList<MutableList<Triple<V, E, Double>>>,
        @SerializedName("vertexListMap") val vertexListMap: MutableList<V>
    )
    override fun <V, E> exportGraph(graph: Graph<V, E>, file: File) {
        val gson = Gson()
        val graphRepresentation = GraphRepresentation(
            mutableListOf<MutableList<Triple<V, E, Double>>>(),
            mutableListOf()
            )
        val vertexList = graphRepresentation.vertexList
        val vertexListMap = graphRepresentation.vertexListMap
        graph.edgeSetOfVertices().forEach { (v1, v2) ->
            val edge = graph.getEdge(v1, v2)!!
            val edgeWeight = graph.getEdgeWeight(edge)

            if (v1 !in vertexListMap) {
                vertexListMap.add(v1)
                vertexList.add(mutableListOf(Triple(v2, edge, edgeWeight)))
            } else {
                val index = vertexListMap.indexOf(v1)
                vertexList[index].add(Triple(v2, edge, edgeWeight))
            }
        }
        val jsonGraph = gson.toJson(graphRepresentation)

        file.writeText(jsonGraph)
    }
}