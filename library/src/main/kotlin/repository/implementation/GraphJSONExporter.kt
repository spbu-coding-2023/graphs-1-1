package repository.implementation

import com.google.gson.Gson
import graph.Graph
import repository.GraphExporter
import java.io.File

class GraphJSONExporter : GraphExporter {
    override fun <V, E> exportGraph(graph: Graph<V, E>, file: File) {
        val gson = Gson()
        val jsonGraph = gson.toJson(graph)
        file.writeText(jsonGraph)
    }
}