package viewModel.workspace.graph.utils

import graph.Graph
import model.EdgeModel
import model.VertexModel
import repository.implementation.json.GraphJSONExporter
import repository.implementation.json.GraphJSONImporter
import java.io.File
import java.io.IOException

class LocalDatabase<V, E>(
    private val graph: Graph<VertexModel<V>, EdgeModel<E>>,
    private val graphName: String
) {
    private val appDir = File(System.getProperty("user.home"), ".graphses") // TODO: probably should be somewhere else

    private val jsonImporter = GraphJSONImporter()
    private val jsonExporter = GraphJSONExporter()

    init {
        if (!appDir.exists()) {
            appDir.mkdirs()
        }

        createFile(graphName)
    }

    fun importGraph() {
        val file = File(appDir, graphName)
        if (file.readText().isEmpty()) return
        jsonImporter.importGraph(graph, file)
    }

    fun exportGraph() {
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