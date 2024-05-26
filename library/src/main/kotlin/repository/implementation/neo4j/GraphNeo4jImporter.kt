package repository.implementation.neo4j

import org.neo4j.driver.AuthTokens
import org.neo4j.driver.Driver
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.Session
import graph.Graph
import repository.DatabaseGraphImporter
import graph.implementation.UndirectedUnweightedGraph

class GraphNeo4jImporter: DatabaseGraphImporter {
    /* Example of credentials
    uri = credentials[0] = "bolt://localhost:7687"
    user = credentials[1] = "neo4j"
    password = credentials[2] = "12345678"
    */
    override fun <V, E> importGraph(graphId: String, credentials: List<String>): Graph<V, E> {
        val driver: Driver = GraphDatabase.driver(credentials[0], AuthTokens.basic(credentials[1], credentials[2]))
        val graph = UndirectedUnweightedGraph<V, E>()
        driver.use {
            val session: Session = it.session()

            // Retrieve the graph metadata
            val graphMetadataResult = session.run("MATCH (g:Graph {id: \$graphId}) RETURN g LIMIT 1")
            if (!graphMetadataResult.hasNext()) {
                throw IllegalStateException("No graph found with id $graphId")
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
            val nodesResult = session.run("MATCH (g:Graph {id: \$graphId})-[:CONTAINS]->(n:Node) RETURN n.value AS value")
            nodesResult.forEach { record ->
                val vertex = record["value"].asObject() as V
                graph.addVertex(vertex)
            }

            // Retrieve all edges and add them to the graph
            val edgesResult = session.run("""
                MATCH (g:Graph {id: \$graphId)-[:CONTAINS]->(r:EDGE)
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
        return graph
    }
}

