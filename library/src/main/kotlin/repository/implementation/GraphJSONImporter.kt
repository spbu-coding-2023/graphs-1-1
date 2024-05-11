package repository.implementation

import com.google.gson.Gson
import graph.Graph
import repository.GraphImporter
import java.io.File

class GraphJSONImporter : GraphImporter {
    override fun <V, E> importGraph(graph: Graph<V, E>, file: File) {
        val gson = Gson()
        val jsonGraph = file.readText()
        gson.fromJson(jsonGraph, graph::class.java)
    }
}