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
        credentials: List<String>
    ): String {
        val driver: Driver = GraphDatabase.driver(credentials[0], AuthTokens.basic(credentials[1], credentials[2]))
        val graphId = UUID.randomUUID().toString()
        driver.use {
            val session: Session = it.session()
            session.executeWrite { tx ->
                // Create a graph node to hold references to all nodes and edges
                tx.run(
                    "CREATE (g:Graph {id: \$graphId, directed: \$directed, weighted: \$weighted}) RETURN g",
                    mapOf("graphId" to graphId, "directed" to graph.configuration.isDirected(), "weighted" to graph.configuration.isWeighted())
                )
                // Export nodes and create relationships to the graph node
                graph.vertexSet().forEach { vertex ->
                    val nodeResult = tx.run(
                        "CREATE (n:Node {value: \$value}) RETURN n",
                        mapOf("value" to vertex)
                    )
                    val nodeId = nodeResult.single()["n"].asNode().elementId()
                    tx.run(
                        "MATCH (g:Graph), (n:Node) WHERE g.id = \$graphId AND id(n) = \$nodeId CREATE (g)-[:CONTAINS]->(n)",
                        mapOf("graphId" to graphId, "nodeId" to nodeId)
                    )
                }

                // Export edges and create relationships to the graph node
                graph.edgeSet().forEach { edge ->
                    val tail = graph.getEdgeTail(edge)
                    val head = graph.getEdgeHead(edge)
                    val weight = graph.getEdgeWeight(edge)
                    val params = mutableMapOf("tail" to tail, "head" to head, "edge" to edge)

                    // Include weight if the graph is weighted
                    if (graph.configuration.isWeighted()) {
                        params["weight"] = weight
                    }

                    val cypher = if (graph.configuration.isWeighted()) {
                        """
                        MATCH (a:Node {value: \$tail}), (b:Node {value: \$head})
                        CREATE (a)-[r:EDGE {value: \$edge, weight: \$weight}]->(b)
                        RETURN r
                        """
                    } else {
                        """
                        MATCH (a:Node {value: \$tail}), (b:Node {value: \$head})
                        CREATE (a)-[r:EDGE {value: \$edge}]->(b)
                        RETURN r
                        """
                    }.trimIndent()

                    val edgeResult = tx.run(cypher, params)
                    val edgeId = edgeResult.single()["r"].asRelationship().elementId()
                    tx.run(
                        "MATCH (g:Graph), ()-[r:EDGE]->() WHERE g.id = \$graphId AND id(r) = \$edgeId CREATE (g)-[:CONTAINS]->(r)",
                        mapOf("graphId" to graphId, "edgeId" to edgeId)
                    )

                    // If the graph is undirected, create the reverse edge
                    if (graph.configuration.isUndirected()) {
                        val reverseEdgeResult = tx.run(cypher, params.apply { put("tail", head); put("head", tail) })
                        val reverseEdgeId = reverseEdgeResult.single()["r"].asRelationship().elementId()
                        tx.run(
                            "MATCH (g:Graph), ()-[r:EDGE]->() WHERE g.id = \$graphId AND id(r) = \$reverseEdgeId CREATE (g)-[:CONTAINS]->(r)",
                            mapOf("graphId" to graphId, "reverseEdgeId" to reverseEdgeId)
                        )
                    }
                }
            }
            session.close()
        }
        return graphId
    }
}

