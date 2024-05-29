package repository.implementation.neo4j

import graph.Graph
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.Driver
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.Session
import repository.DatabaseGraphExporter
import java.util.UUID

class GraphNeo4jExporter: DatabaseGraphExporter {
    /* Example of credentials
    uri = credentials[0] = "bolt://localhost:7687"
    user = credentials[1] = "neo4j"
    password = credentials[2] = "12345678"
    */
    override fun <V, E> exportGraph(
        graph: Graph<V, E>,
        credentials: List<String?>,
        graphId: String
    ): String {
        if (credentials[0] == null) {
            throw IllegalStateException("bolt link needed (credentials[0])")
        }
        val driver: Driver = GraphDatabase.driver(credentials[0], AuthTokens.basic(credentials[1], credentials[2]))
        driver.use {
            val session: Session = it.session()
            session.executeWrite { tx ->
                // Create a graph node containing metadata
                tx.run(
                    "CREATE (g:Graph {id: \$graphId, directed: \$directed, weighted: \$weighted}) RETURN g",
                    mapOf("graphId" to graphId, "directed" to graph.configuration.isDirected(), "weighted" to graph.configuration.isWeighted())
                )
                // Export nodes with graphId
                graph.vertexSet().forEach { vertex ->
                    tx.run(
                        "CREATE (n:Node {graphId: \$graphId, value: \$value}) RETURN n",
                        mapOf("graphId" to graphId, "value" to vertex)
                    )
                }

                // Export edges and create relationships to the graph node
                graph.edgeSet().forEach { edge ->
                    val tail = graph.getEdgeTail(edge)
                    val head = graph.getEdgeHead(edge)
                    val weight = graph.getEdgeWeight(edge)

                    if (graph.configuration.isWeighted()) {
                        tx.run(
                                "MATCH (a:Node {graphId: \$graphId, value: \$tail}), (b:Node {graphId: \$graphId, value: \$head}) " +
                                "CREATE (a)-[r:EDGE {graphId: \$graphId, value: \$edge, weight: \$weight}]->(b) " +
                                "RETURN r", mapOf("graphId" to graphId, "tail" to tail, "head" to head, "edge" to edge, "weight" to weight))
                    } else {
                        tx.run(
                                "MATCH (a:Node {graphId: \$graphId, value: \$tail}), (b:Node {graphId: \$graphId, value: \$head}) " +
                                "CREATE (a)-[r:EDGE {graphId: \$graphId, value: \$edge}]->(b) " +
                                "RETURN r", mapOf("graphId" to graphId, "tail" to tail, "head" to head, "edge" to edge))
                        }

                    // If the graph is undirected, create the reverse edge
                    if (graph.configuration.isUndirected()) {
                        if (graph.configuration.isWeighted()) {
                            tx.run(
                                    "MATCH (a:Node {graphId: \$graphId, value: \$head}), (b:Node {graphId: \$graphId, value: \$tail}) " +
                                    "CREATE (a)-[r:EDGE {graphId: \$graphId, value: \$edge, weight: \$weight}]->(b) " +
                                    "RETURN r", mapOf("graphId" to graphId, "tail" to tail, "head" to head, "edge" to edge, "weight" to weight))
                        } else {
                            tx.run(
                                    "MATCH (a:Node {graphId: \$graphId, value: \$head}), (b:Node {graphId: \$graphId, value: \$tail}) " +
                                    "CREATE (a)-[r:EDGE {graphId: \$graphId, value: \$edge}]->(b) " +
                                    "RETURN r", mapOf("graphId" to graphId, "tail" to tail, "head" to head, "edge" to edge))
                        }
                    }
                }
            }
            session.close()
        }
        return graphId
    }
}

