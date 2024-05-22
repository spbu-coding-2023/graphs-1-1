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
        driver.use {
            val session: Session = it.session()

            // Retrieve all nodes
            val nodesResult = session.run("MATCH (n) RETURN n")
            val nodes = mutableMapOf<String, V>()
            while (nodesResult.hasNext()) {
                val record = nodesResult.next()
                val node = record["n"].asNode()
                val id = node.elementId().toString()
                val value = node["value"].asObject() as V
                nodes[id] = value
                graph.addVertex(value)
            }

            // Retrieve all relationships
            val edgesResult = session.run("MATCH (a)-[r:EDGE]->(b) RETURN a, b, r")
            while (edgesResult.hasNext()) {
                val record = edgesResult.next()
                val nodeA = record["a"].asNode()
                val nodeB = record["b"].asNode()
                val relationship = record["r"].asRelationship()

                val valueA = nodes[nodeA.elementId().toString()]!!
                val valueB = nodes[nodeB.elementId().toString()]!!
                val edge = relationship["value"].asObject() as E
                val weight = relationship["weight"].asDouble()

                graph.addEdge(valueA, valueB, edge, weight)
            }
            session.close()
        }
    }
}
