package repository.implementation.sqlite

import graph.Graph
import repository.GraphExporter
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

class GraphSQLiteExporter : GraphExporter {
    override fun <V, E> exportGraph(graph: Graph<V, E>, file: File) {
        val url = "jdbc:sqlite:${file.absolutePath}"
        val connection: Connection = DriverManager.getConnection(url)

        connection.use {
            it.createStatement().use { statement ->
                statement.execute("DROP TABLE IF EXISTS Configuration")
                statement.execute("DROP TABLE IF EXISTS Node")
                statement.execute("DROP TABLE IF EXISTS Edge")
                statement.execute("CREATE TABLE Configuration (isDirected INTEGER, isWeighted INTEGER)")
                statement.execute("CREATE TABLE Node (id INTEGER PRIMARY KEY, value TEXT)")
                statement.execute("CREATE TABLE Edge (tail INTEGER, head INTEGER, value TEXT, weight REAL, FOREIGN KEY(tail) REFERENCES Node(id), FOREIGN KEY(head) REFERENCES Node(id))")
            }

            val insertConfigSQL = "INSERT INTO Configuration(isDirected, isWeighted) VALUES (?, ?)"
            val configStatement: PreparedStatement = connection.prepareStatement(insertConfigSQL)
            configStatement.setInt(1, if (graph.configuration.isDirected()) 1 else 0)
            configStatement.setInt(2, if (graph.configuration.isWeighted()) 1 else 0)
            configStatement.executeUpdate()

            val insertNodeSQL = "INSERT INTO Node(value) VALUES (?)"
            val nodeStatement: PreparedStatement = connection.prepareStatement(insertNodeSQL)
            val nodeMap = mutableMapOf<V, Int>()
            graph.vertexSet().forEachIndexed { index, vertex ->
                nodeStatement.setString(1, vertex.toString())
                nodeStatement.executeUpdate()
                nodeMap[vertex] = index + 1
            }

            val insertEdgeSQL = "INSERT INTO Edge(tail, head, value, weight) VALUES (?, ?, ?, ?)"
            val edgeStatement: PreparedStatement = connection.prepareStatement(insertEdgeSQL)
            graph.edgeSet().forEach { edge ->
                val tail = graph.getEdgeTail(edge)
                val head = graph.getEdgeHead(edge)
                val weight = graph.getEdgeWeight(edge)

                edgeStatement.setInt(1, nodeMap[tail]!!)
                edgeStatement.setInt(2, nodeMap[head]!!)
                edgeStatement.setString(3, edge.toString())
                edgeStatement.setDouble(4, weight)
                edgeStatement.executeUpdate()
            }
        }
    }
}
