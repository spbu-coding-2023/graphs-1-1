package view.settings

import viewModel.workspace.graph.utils.LocalDatabase
import java.io.File
import java.io.IOException
import kotlinx.serialization.Serializable

class SettingsFileManager(
) {
    private val settingsFileName = ".settings.json"
    @Serializable
    data class settings(
        val sqlitePath: String,
        val neo4jUrl: String,
        val neo4jUser: String,
        val neo4jPassword: String
    )
    private val appDir = LocalDatabase.getGraphsDirectory()

    init {
        if (!appDir.exists()) {
            appDir.mkdirs()
        }

        createFile(settingsFileName)
    }

    fun saveSettings(settings: String) {
        writeFile(settingsFileName, settings)
    }

    fun loadSettings(): String {
        return readFile(settingsFileName)
    }

    fun deleteSettings(): Boolean {
        return deleteFile(settingsFileName)
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
