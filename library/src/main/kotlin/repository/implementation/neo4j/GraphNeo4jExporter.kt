package repository.implementation.neo4j

import graph.Graph
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.Driver
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.Session
import repository.GraphExporter
import java.io.File

class GraphNeo4jExporter : GraphExporter {
    private val uri = "bolt://localhost:7687"
    private val user = "neo4j"
    private val password = "12345678"

    override fun <V, E> exportGraph(graph: Graph<V, E>, file: File) {
        val driver: Driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
        driver.use {
            val session: Session = it.session()
            session.executeWrite { tx ->
                // Export nodes
                graph.vertexSet().forEach { vertex ->
                    tx.run("CREATE (n:Node {value: \$value})", mapOf("value" to vertex))
                }

                // Export edges
                graph.edgeSet().forEach { edge ->
                    val tail = graph.getEdgeTail(edge)
                    val head = graph.getEdgeHead(edge)
                    val weight = graph.getEdgeWeight(edge)

                    tx.run(
                        """
                        MATCH (a:Node {value: \$tail})
                        MATCH (b:Node {value: \$head})
                        CREATE (a)-[:EDGE {value: \$edge, weight: \$weight}]->(b)
                        """.trimIndent(),
                        mapOf("tail" to tail, "head" to head, "edge" to edge, "weight" to weight)
                    )
                }
            }
            session.close()
        }
    }
}