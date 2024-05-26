package repository.implementation.sqlite

import graph.Graph
import repository.GraphImporter
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

class GraphSQLiteImporter : GraphImporter {
    override fun <V, E> importGraph(graph: Graph<V, E>, file: File) {
        val url = "jdbc:sqlite:${file.absolutePath}"
        val connection: Connection = DriverManager.getConnection(url)

        connection.use {
            val nodeQuery = "SELECT id, value FROM Node"
            val nodeStatement = connection.createStatement()
            val nodeResultSet = nodeStatement.executeQuery(nodeQuery)
            val nodeMap = mutableMapOf<Int, V>()

            while (nodeResultSet.next()) {
                val id = nodeResultSet.getInt("id")
                val value = nodeResultSet.getString("value")
                val vertex: V = value as V
                graph.addVertex(vertex)
                nodeMap[id] = vertex
            }

            val edgeQuery = "SELECT tail, head, value, weight FROM Edge"
            val edgeStatement = connection.createStatement()
            val edgeResultSet = edgeStatement.executeQuery(edgeQuery)

            while (edgeResultSet.next()) {
                val tailId = edgeResultSet.getInt("tail")
                val headId = edgeResultSet.getInt("head")
                val value = edgeResultSet.getString("value")
                val weight = edgeResultSet.getDouble("weight")
                val tail = nodeMap[tailId]!!
                val head = nodeMap[headId]!!
                val edge: E = value as E
                graph.addEdge(tail, head, edge)
                graph.setEdgeWeight(edge, weight)
            }
        }
    }
}
