package viewModel.workspace.graph.utils

import graph.Graph
import model.EdgeModel
import model.VertexModel
import repository.implementation.json.GraphJSONExporter
import repository.implementation.json.GraphJSONImporter
import java.io.File
import java.io.IOException

class GraphStorage(
    private val getGraph: () -> Graph<VertexModel, EdgeModel>,
    private val graphName: String
) {
    private val appDir = LocalDatabase.getGraphsDirectory() // TODO: probably should be somewhere else

    private val jsonImporter = GraphJSONImporter()
    private val jsonExporter = GraphJSONExporter()

    init {
        if (!appDir.exists()) {
            appDir.mkdirs()
        }

        val file = File(appDir, graphName)
        if (!file.exists()) {
            createFile(graphName)
            exportGraph()
        } else {
            importGraph()
        }

    }

    fun importGraph() {
        var file = File(appDir, graphName)
        if (!file.exists()) {
            file = createFile(graphName)
        }
        val graph = getGraph()
        jsonImporter.importGraph(graph, file, VertexModel::class.java, EdgeModel::class.java)
    }

    fun exportGraph() {
        val graph = getGraph()
        val file = File(appDir, graphName)
        jsonExporter.exportGraph(graph, file)
    }

    fun deleteGraph(): Boolean {
        return deleteFile(graphName)
    }

    private fun createFile(fileName: String): File {
        val file = File(appDir, fileName)
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    private fun writeFile(fileName: String, data: String) {
        val file = File(appDir, fileName)
        try {
            file.writeText(data)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun readFile(fileName: String): String {
        val file = File(appDir, fileName)
        return try {
            file.readText()
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    private fun deleteFile(fileName: String): Boolean {
        val file = File(appDir, fileName)
        return file.delete()
    }
}