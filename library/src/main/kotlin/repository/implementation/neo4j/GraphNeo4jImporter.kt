package repository.implementation.neo4j

import graph.Graph
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.Driver
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.Session
import repository.GraphImporter
import java.io.File

class GraphNeo4jImporter : GraphImporter {
    private val uri = "bolt://localhost:7687"
    private val user = "neo4j"
    private val password = "12345678"

    override fun <V, E> importGraph(graph: Graph<V, E>, file: File) {
        val driver: Driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
        return driver.use {
            val session: Session = it.session()

            // Retrieve the graph metadata
            val graphMetadataResult = session.run("MATCH (g:Graph) RETURN g LIMIT 1") // TODO: have the user choose what graph to import, perhaps by changing that wretched file argument
            if (!graphMetadataResult.hasNext()) {
                throw IllegalStateException("No graph found in the database.")
            }
            val graphMetadata = graphMetadataResult.single()["g"].asNode()
            val isDirected = graphMetadata["directed"].asBoolean()
            val isWeighted = graphMetadata["weighted"].asBoolean()

            // Change metadata of the graph
            if (isWeighted) {
                graph.configuration.asWeighted()
            } else {
                graph.configuration.asUnweighted()
            }
            if (isDirected) {
                graph.configuration.isDirected()
            } else {
                graph.configuration.asUndirected()
            }

            // Retrieve all nodes and add them to the graph
            val nodesResult = session.run("MATCH (g:Graph)-[:CONTAINS]->(n:Node) RETURN n.value AS value")
            nodesResult.forEach { record ->
                val vertex = record["value"].asObject() as V
                graph.addVertex(vertex)
            }

            // Retrieve all edges and add them to the graph
            val edgesResult = session.run("""
                MATCH (g:Graph)-[:CONTAINS]->(r:EDGE)
                MATCH (a:Node)-[r]->(b:Node)
                RETURN id(r) AS id, a.value AS tail, b.value AS head, r.value AS value, r.weight AS weight
            """.trimIndent())
            edgesResult.forEach { record ->
                val tail = record["tail"].asObject() as V
                val head = record["head"].asObject() as V
                val edge = record["value"].asObject() as E

                if (isWeighted) {
                    val weight = record["weight"].asDouble()
                    graph.addEdge(tail, head, edge, weight)
                } else {
                    graph.addEdge(tail, head, edge)
                }
            }

            session.close()
        }
    }
}

