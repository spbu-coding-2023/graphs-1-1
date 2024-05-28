package repository.implementation.neo4j

import org.neo4j.driver.AuthTokens
import org.neo4j.driver.Driver
import org.neo4j.driver.Session
import graph.Graph
import graph.implementation.UndirectedUnweightedGraph
import repository.DatabaseGraphImporter

class GraphNeo4jImporter(private val driverFactory: (String, org.neo4j.driver.AuthToken) -> Driver): DatabaseGraphImporter {
    /* Example of credentials
    uri = credentials[0] = "bolt://localhost:7687"
    user = credentials[1] = "neo4j"
    password = credentials[2] = "12345678"
    */
    override fun <V, E> importGraph(graphId: String, credentials: List<String?>): Graph<V, E> {
        if (credentials[0] == null) {
            throw IllegalStateException("bolt link needed (credentials[0])")
        }
        val driver: Driver = driverFactory(credentials[0]!!, AuthTokens.basic(credentials[1], credentials[2]))
        val graph = UndirectedUnweightedGraph<V, E>()
        driver.use {
            val session: Session = driver.session()

            // Retrieve the graph metadata
            val graphMetadataResult = session.run("MATCH (g:Graph {id: \$graphId}) RETURN g LIMIT 1", mapOf("graphId" to graphId))
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
                graph.configuration.asDirected()
            } else {
                graph.configuration.asUndirected()
            }

            // Retrieve all nodes and add them to the graph
            val nodesResult = session.run("MATCH (n:Node {graphId: \$graphId}) RETURN n.value AS value", mapOf("graphId" to graphId))
            nodesResult.forEach { record ->
                val vertex = record["value"].asObject() as V
                graph.addVertex(vertex)
            }

            // Retrieve all edges and add them to the graph
            val edgesResult = session.run(
                "MATCH (a)-[r:EDGE {graphId: \$graphId}]->(b) " +
                "RETURN a.value AS tail, b.value AS head, r.value AS edge, r.weight AS weight",
                mapOf("graphId" to graphId))
            edgesResult.forEach { record ->
                val tail = record["tail"].asObject() as V
                val head = record["head"].asObject() as V
                val edge = record["edge"].asObject() as E

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

