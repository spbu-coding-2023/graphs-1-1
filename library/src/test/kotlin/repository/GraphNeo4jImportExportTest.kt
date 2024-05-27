import graph.Graph
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.neo4j.driver.Driver
import org.neo4j.driver.*
import org.neo4j.driver.types.Node
import graph.implementation.DirectedUnweightedGraph
import graph.implementation.DirectedWeightedGraph
import graph.implementation.UndirectedUnweightedGraph
import graph.implementation.UndirectedWeightedGraph
import repository.implementation.neo4j.GraphNeo4jExporter
import repository.implementation.neo4j.GraphNeo4jImporter
import org.testcontainers.containers.Neo4jContainer
import org.testcontainers.utility.DockerImageName

class GraphNeo4jImporterTest {

    @Test
    fun `mock neo4j and importGraph should return graph with nodes and edges`() {

        // Mock credentials and graph id
        val graphId = "graphId"
        val credentials = listOf("bolt://localhost:7687", "neo4j", "password")

        // Mock Neo4j driver and session
        val driver = mock(Driver::class.java)
        val driverFactory: (String, AuthToken) -> Driver = { _, _ -> driver }
        val session = mock(Session::class.java)

        // Mock result for graph metadata
        val graphMetadataResult = mock(Result::class.java)
        val graphMetadataRecord = mock(Record::class.java)
        val graphMetadataNode = mock(Node::class.java)
        val graphMetadataValue = mock(Value::class.java)

        `when`(graphMetadataResult.hasNext()).thenReturn(true)
        `when`(graphMetadataResult.single()).thenReturn(graphMetadataRecord)
        `when`(graphMetadataRecord.get("g")).thenReturn(graphMetadataValue)
        `when`(graphMetadataValue.asNode()).thenReturn(graphMetadataNode)
        `when`(graphMetadataNode["directed"]).thenReturn(Values.value(true))
        `when`(graphMetadataNode["weighted"]).thenReturn(Values.value(true))
        `when`(session.run("MATCH (g:Graph {id: \$graphId}) RETURN g LIMIT 1", mapOf("graphId" to graphId))).thenReturn(graphMetadataResult)


        // Mock result for nodes
        val nodesResult = mock(Result::class.java)
        val nodeRecord1 = mock(Record::class.java)
        val nodeRecord2 = mock(Record::class.java)
        val node1 = mock(Value::class.java)
        `when`(node1.asObject()).thenReturn("node1")
        val node2 = mock(Value::class.java)
        `when`(node2.asObject()).thenReturn("node2")

        `when`(nodeRecord1["value"]).thenReturn(node1)
        `when`(nodeRecord2["value"]).thenReturn(node2)

        `when`(nodesResult.hasNext()).thenReturn(true, true, false)
        `when`(nodesResult.next()).thenReturn(nodeRecord1, nodeRecord2)
        `when`(session.run("MATCH (n:Node {graphId: \$graphId}) RETURN n.value AS value", mapOf("graphId" to graphId))).thenReturn(
            nodesResult
        )

        // Mock result for edges
        val edgesResult = mock(Result::class.java)
        val edgeRecord1 = mock(Record::class.java)
        val edgeRecord2 = mock(Record::class.java)
        val edge1 = mock(Value::class.java)
        `when`(edge1.asObject()).thenReturn("edge1")
        val edge2 = mock(Value::class.java)
        `when`(edge2.asObject()).thenReturn("edge2")

        `when`(edgeRecord1["tail"]).thenReturn(node1)
        `when`(edgeRecord2["tail"]).thenReturn(node2)
        `when`(edgeRecord1["head"]).thenReturn(node2)
        `when`(edgeRecord2["head"]).thenReturn(node1)
        `when`(edgeRecord1["edge"]).thenReturn(edge1)
        `when`(edgeRecord2["edge"]).thenReturn(edge2)

        val weightValue = mock(Value::class.java)
        `when`(weightValue.asDouble()).thenReturn(1.0)
        `when`(edgeRecord1["weight"]).thenReturn(weightValue)
        `when`(edgeRecord2["weight"]).thenReturn(weightValue)

        `when`(edgesResult.hasNext()).thenReturn(true, true, false)
        `when`(edgesResult.next()).thenReturn(edgeRecord1, edgeRecord2)
        `when`(
            session.run(
                  "MATCH (a)-[r:EDGE {graphId: \$graphId}]->(b) " +
                        "RETURN a.value AS tail, b.value AS head, r.value AS edge, r.weight AS weight",
                mapOf("graphId" to graphId)
            )
        ).thenReturn(edgesResult)

        // Mock driver behavior
        `when`(driver.session()).thenReturn(session)

        // Instantiate GraphNeo4jImporter
        val importer = GraphNeo4jImporter(driverFactory)

        // Call importGraph function
        val result = importer.importGraph<String, String>(graphId, credentials)

        // Verify the graph configuration
        assertEquals(true, result.configuration.isDirected())
        assertEquals(true, result.configuration.isWeighted())

        // Verify the nodes and edges are added to the graph
        assertEquals(true, result.containsVertex("node1"))
        assertEquals(true, result.containsVertex("node2"))
        assertEquals(true, result.containsEdge("node1", "node2"))
        assertEquals(true, result.containsEdge("node2", "node1"))
        assertEquals(1.0, result.getEdgeWeight("edge1"))
    }

    @Test
    fun `mock neo4j and importGraph should throw IllegalStateException when no graph found`() {
        // Mock credentials and graph id
        val graphId = "graphId"
        val credentials = listOf("bolt://localhost:7687", "neo4j", "password")

        // Mock Neo4j driver and session
        val driver = mock(Driver::class.java)
        val driverFactory: (String, AuthToken) -> Driver = { _, _ -> driver }
        val session = mock(Session::class.java)

        // Mock result for graph metadata
        val graphMetadataResult = mock(Result::class.java)
        `when`(graphMetadataResult.hasNext()).thenReturn(false)
        `when`(session.run("MATCH (g:Graph {id: \$graphId}) RETURN g LIMIT 1", mapOf("graphId" to graphId))).thenReturn(graphMetadataResult)

        // Mock driver behavior
        `when`(driver.session()).thenReturn(session)

        // Instantiate GraphNeo4jImporter
        val importer = GraphNeo4jImporter(driverFactory)

        // Call importGraph function and expect IllegalStateException
        assertThrows(IllegalStateException::class.java) {
            importer.importGraph<String, String>(graphId, credentials)
        }
    }

    @Test
    fun `bolt link is absent so it should throw exception`() {
        val uri: String? = null
        val user = "john"
        val password = "password"
        val driver = mock(Driver::class.java)
        val driverFactory: (String, AuthToken) -> Driver = { _, _ -> driver }
        assertThrows(IllegalStateException::class.java) {
            GraphNeo4jImporter(driverFactory).importGraph<String, String>("id", listOf(uri, user, password))
        }
    }

    private fun setupDocker (graph: Graph<String, String>): Graph<String, String> {
        // Start a Neo4j container for testing
        val neo4jContainer = Neo4jContainer<Nothing>(DockerImageName.parse("neo4j:latest"))
        neo4jContainer.withRandomPassword()
        neo4jContainer.start()

        // Get the Neo4j container's connection details
        val uri = neo4jContainer.boltUrl
        val user = "neo4j"
        val password = neo4jContainer.adminPassword

        // Create a test graph
        graph.addVertex("A")
        graph.addVertex("B")
        graph.addVertex("C")
        graph.addEdge("A", "B", "AB", 3.0)
        graph.addEdge("B", "C", "BC", 4.0)
        graph.addEdge("C", "A", "CA", 5.0)

        // Instantiate GraphNeo4jExporter
        val exporter = GraphNeo4jExporter()

        // Export test graph
        val graphId = exporter.exportGraph(graph, listOf(uri, user, password))

        // Instantiate GraphNeo4jImporter
        val driverFactory: (String, AuthToken) -> Driver = { currentUri, authToken ->
            GraphDatabase.driver(currentUri, authToken)
        }
        val importer = GraphNeo4jImporter(driverFactory)

        // Import the test graph back from Neo4j
        return importer.importGraph(graphId, listOf(uri, user, password))
    }

    @Test
    fun `docker container neo4j and importGraph should import DWGraph nodes with edges`() {
        val resultingGraph = setupDocker(DirectedWeightedGraph())
        assertEquals(true, resultingGraph.configuration.isWeighted())
        assertEquals(true, resultingGraph.configuration.isDirected())
        assertEquals(true, resultingGraph.containsVertex("A"))
        assertEquals(true, resultingGraph.containsVertex("B"))
        assertEquals(true, resultingGraph.containsVertex("C"))
        assertEquals(true, resultingGraph.containsEdge("A", "B"))
        assertEquals(true, resultingGraph.containsEdge("B", "C"))
        assertEquals(true, resultingGraph.containsEdge("C", "A"))
        assertEquals(false, resultingGraph.containsEdge("B", "A"))
        assertEquals(false, resultingGraph.containsEdge("C", "B"))
        assertEquals(false, resultingGraph.containsEdge("A", "C"))
        assertEquals(3.0, resultingGraph.getEdgeWeight("A", "B"))
        assertEquals(4.0, resultingGraph.getEdgeWeight("B", "C"))
        assertEquals(5.0, resultingGraph.getEdgeWeight("C", "A"))
    }

    @Test
    fun `docker container neo4j and importGraph should import UDUWGraph nodes with edges`() {
        val resultingGraph = setupDocker(UndirectedUnweightedGraph())
        assertEquals(true, resultingGraph.configuration.isUnweighted())
        assertEquals(true, resultingGraph.configuration.isUndirected())
        assertEquals(true, resultingGraph.containsVertex("A"))
        assertEquals(true, resultingGraph.containsVertex("B"))
        assertEquals(true, resultingGraph.containsVertex("C"))
        assertEquals(true, resultingGraph.containsEdge("A", "B"))
        assertEquals(true, resultingGraph.containsEdge("B", "C"))
        assertEquals(true, resultingGraph.containsEdge("C", "A"))
        assertEquals(true, resultingGraph.containsEdge("B", "A"))
        assertEquals(true, resultingGraph.containsEdge("C", "B"))
        assertEquals(true, resultingGraph.containsEdge("A", "C"))
        assertEquals(resultingGraph.DEFAULT_EDGE_WEIGHT, resultingGraph.getEdgeWeight("A", "B"))
        assertEquals(resultingGraph.DEFAULT_EDGE_WEIGHT, resultingGraph.getEdgeWeight("B", "C"))
        assertEquals(resultingGraph.DEFAULT_EDGE_WEIGHT, resultingGraph.getEdgeWeight("A", "C"))
        assertEquals(resultingGraph.DEFAULT_EDGE_WEIGHT, resultingGraph.getEdgeWeight("B", "A"))
        assertEquals(resultingGraph.DEFAULT_EDGE_WEIGHT, resultingGraph.getEdgeWeight("C", "B"))
        assertEquals(resultingGraph.DEFAULT_EDGE_WEIGHT, resultingGraph.getEdgeWeight("C", "A"))
    }
}

class GraphNeo4jExporterTest {
    private fun setupDocker(graph: Graph<String, String>) {
        // Start a Neo4j container for testing
        val neo4jContainer = Neo4jContainer<Nothing>(DockerImageName.parse("neo4j:latest"))
        neo4jContainer.withRandomPassword()
        neo4jContainer.start()

        // Get the Neo4j container's connection details
        val uri = neo4jContainer.boltUrl
        val user = "neo4j"
        val password = neo4jContainer.adminPassword

        // Create a test graph
        graph.addVertex("A")
        graph.addVertex("B")
        graph.addVertex("C")
        graph.addEdge("A", "B", "AB", 3.0)
        graph.addEdge("B", "C", "BC", 4.0)
        graph.addEdge("C", "A", "CA", 5.0)

        // Instantiate GraphNeo4jExporter
        val exporter = GraphNeo4jExporter()

        // Call exportGraph function
        val graphId =  exporter.exportGraph(graph, listOf(uri, user, password))

        // Verify the data
        val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
        driver.use {
            val session = it.session()
            val result = session.run(
                "MATCH (a)-[r:EDGE {graphId: \$graphId}]->(b) " +
                        "RETURN r.graphId as graphId, a.value AS tail, b.value AS head, r.value AS value, r.weight AS weight",
                mapOf("graphId" to graphId)
            )
            // Initialize expected data
            val edgesSet: MutableSet<Pair<String, String>>
            val vertexList: MutableList<String>
            if (graph.configuration.isUndirected()) {
                edgesSet = mutableSetOf(Pair("A", "B"), Pair("B", "A"), Pair("B", "C"), Pair("C", "B"), Pair("C", "A"), Pair("A", "C"))
                vertexList = mutableListOf("A", "A", "B", "B", "C", "C")
            } else {
                edgesSet = mutableSetOf(Pair("A", "B"), Pair("B", "C"), Pair("C", "A"))
                vertexList = mutableListOf("A", "B", "C")
            }

            // Process the result and verify it against the test graph
            while (result.hasNext()) {
                val record = result.next()
                val tail = record["tail"].asString()
                val head = record["head"].asString()
                val value = record["value"].asString()
                val weight: Double
                if (record["weight"].isNull) {
                    weight = graph.DEFAULT_EDGE_WEIGHT
                } else {
                    weight = record["weight"].asDouble()
                }

                // Perform assertions to ensure the data matches the test graph
                assertEquals(true, graph.containsVertex(tail))
                vertexList.remove(tail)
                assertEquals(true, graph.containsVertex(head))
                vertexList.remove(head)
                assertEquals(true, graph.containsEdge(tail, head))
                edgesSet.remove(Pair(tail, head))
                assertEquals(value, graph.getEdge(tail, head))
                if (graph.configuration.isDirected()) {
                    assertEquals(false, graph.containsEdge(head, tail))
                }
                if (graph.configuration.isWeighted()) {
                    assertEquals(graph.getEdgeWeight(tail, head), weight)
                } else {
                    assertEquals(graph.DEFAULT_EDGE_WEIGHT, weight)
                }
            }
            assertEquals(setOf<Pair<String, String>>(), edgesSet)
            assertEquals(listOf<String>(), vertexList)

            // Stop the Neo4j container after the test
            neo4jContainer.stop()
        }
    }

    @Test
    fun `docker container neo4j and exportGraph should export DWGraph nodes and edges to Neo4j`() {
        setupDocker(DirectedWeightedGraph())
    }

    @Test
    fun `docker container neo4j and exportGraph should export UDUWGraph nodes and edges to Neo4j`() {
        setupDocker(UndirectedUnweightedGraph())
    }

    @Test
    fun `docker container neo4j and exportGraph should export DUWGraph nodes and edges to Neo4j`() {
        setupDocker(DirectedUnweightedGraph())
    }

    @Test
    fun `docker container neo4j and exportGraph should export UDWGraph nodes and edges to Neo4j`() {
        setupDocker(UndirectedWeightedGraph())
    }

    @Test
    fun `bolt link is absent`() {
        val testGraph = DirectedWeightedGraph<String, String>()
        val uri : String? = null
        val user = "john"
        val password = "password"
        assertThrows(IllegalStateException::class.java) {
            GraphNeo4jExporter().exportGraph(testGraph, listOf(uri, user, password))
        }
    }
}


