package repository

import graph.implementation.DirectedUnweightedGraph
import graph.implementation.DirectedWeightedGraph
import graph.implementation.UndirectedUnweightedGraph
import graph.implementation.UndirectedWeightedGraph
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import repository.implementation.sqlite.GraphSQLiteExporter
import repository.implementation.sqlite.GraphSQLiteImporter
import java.io.File

class GraphSQLiteImportExportTest{
    var DWgraph = DirectedWeightedGraph<Int, String>()
    var UDWgraph = UndirectedWeightedGraph<Int, String>()
    var DWgraphNew = DirectedWeightedGraph<Int, String>()
    var UDWgraphNew = UndirectedWeightedGraph<Int, String>()
    var DUWgraph = DirectedUnweightedGraph<Int, String>()
    var DUWgraphNew = DirectedUnweightedGraph<Int, String>()
    var UDUWgraph = UndirectedUnweightedGraph<Int, String>()
    var UDUWgraphNew = UndirectedUnweightedGraph<Int, String>()
    var file = File.createTempFile("SQLiteGraph", "sqlite")
    val exporter = GraphSQLiteExporter()
    val importer = GraphSQLiteImporter()

    @BeforeEach
    fun setup() {
        DWgraph = DirectedWeightedGraph()
        DWgraphNew = DirectedWeightedGraph()
        UDWgraph = UndirectedWeightedGraph()
        UDWgraphNew = UndirectedWeightedGraph()
        DUWgraph = DirectedUnweightedGraph()
        DUWgraphNew = DirectedUnweightedGraph()
        UDUWgraph = UndirectedUnweightedGraph()
        UDUWgraphNew = UndirectedUnweightedGraph()
        file = File.createTempFile("SQLiteGraph", "sqlite")

    }

    @AfterEach
    fun cleanUp() {
        file.deleteOnExit()
    }

    @Test
    fun `empty graph`() {
        exporter.exportGraph(DWgraph, file)
        importer.importGraph(DWgraphNew, file)
        assertEquals(DWgraphNew.vertexSet(), setOf<Int>())
        assertEquals(DWgraphNew.edgeSet(), setOf<String>())
    }

    @Test
    fun `single vertex graph`() {
        DWgraph.addVertex(23)
        exporter.exportGraph(DWgraph, file)
        importer.importGraph(DWgraphNew, file)
        assertEquals(DWgraphNew.vertexSet(), setOf(23))
        assertEquals(DWgraphNew.edgeSet(), setOf<String>())
    }

    @Test
    fun `single edge graph`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addEdge(23, 52, "A")
        exporter.exportGraph(DWgraph, file)
        importer.importGraph(DWgraphNew, file)

        assertEquals(DWgraphNew.vertexSet(), setOf(23, 52))
        assertEquals(DWgraphNew.edgeSet(), setOf("A"))
    }

    @Test
    fun `single edge graph with changed weight`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addEdge(23, 52, "A", 91725.2345)

        exporter.exportGraph(DWgraph, file)
        importer.importGraph(DWgraphNew, file)

        assertEquals(DWgraphNew.vertexSet(), setOf(23, 52))
        assertEquals(DWgraphNew.edgeSet(), setOf("A"))
        assertEquals(DWgraphNew.getEdgeWeight(23, 52), 91725.2345)
    }

    @Test
    fun `complex graph directed`() {
        DWgraph.addVertex(1)
        DWgraph.addVertex(2)
        DWgraph.addVertex(3)

        DWgraph.addEdge(1, 2, "A")
        DWgraph.addEdge(1, 3, "C")
        DWgraph.addEdge(2, 3, "B")

        exporter.exportGraph(DWgraph, file)
        importer.importGraph(DWgraphNew, file)

        assertEquals(DWgraphNew.vertexSet(), setOf(1, 2, 3))
        assertEquals(DWgraphNew.edgeSet(), setOf("A", "B", "C"))
        assertEquals(DWgraphNew.getEdge(1, 2), "A")
        assertEquals(DWgraphNew.getEdge(2, 3), "B")
        assertEquals(DWgraphNew.getEdge(1, 3), "C")
        assertTrue(DWgraphNew.configuration.isDirected())
        assertTrue(DWgraphNew.configuration.isWeighted())
        assertTrue(DWgraph.configuration.isDirected())
        assertTrue(DWgraph.configuration.isWeighted())
    }

    @Test
    fun `complex graph undirected`() {
        UDWgraph.addVertex(1)
        UDWgraph.addVertex(2)
        UDWgraph.addVertex(3)

        UDWgraph.addEdge(1, 2, "A")
        UDWgraph.addEdge(1, 3, "C")
        UDWgraph.addEdge(2, 3, "B")

        exporter.exportGraph(UDWgraph, file)
        importer.importGraph(UDWgraphNew, file)

        assertEquals(UDWgraphNew.vertexSet(), setOf(1, 2, 3))
        assertEquals(UDWgraphNew.edgeSet(), setOf("A", "B", "C"))
        assertEquals(UDWgraphNew.getEdge(1, 2), "A")
        assertEquals(UDWgraphNew.getEdge(2, 1), "A")
        assertEquals(UDWgraphNew.getEdge(2, 3), "B")
        assertEquals(UDWgraphNew.getEdge(3, 2), "B")
        assertEquals(UDWgraphNew.getEdge(1, 3), "C")
        assertEquals(UDWgraphNew.getEdge(3, 1), "C")
        assertTrue(UDWgraphNew.configuration.isUndirected())
        assertTrue(UDWgraphNew.configuration.isWeighted())
        assertTrue(UDWgraph.configuration.isUndirected())
        assertTrue(UDWgraph.configuration.isWeighted())
    }

    @Test
    fun `self loop`() {
        DWgraph.addVertex(23)
        DWgraph.addEdge(23, 23, "A")

        exporter.exportGraph(UDWgraph, file)
        importer.importGraph(UDWgraphNew, file)

        assertEquals(DWgraph.vertexSet(), setOf(23))
        assertEquals(DWgraph.edgeSet(), setOf("A"))
        assertEquals(DWgraph.getEdge(23, 23), "A")
    }

    @Test
    fun `weighted graph cast undirected to directed`() {
        UDWgraph.addVertex(1)
        UDWgraph.addVertex(2)
        UDWgraph.addVertex(3)

        UDWgraph.addEdge(1, 2, "A")
        UDWgraph.addEdge(1, 3, "C")
        UDWgraph.addEdge(2, 3, "B")

        exporter.exportGraph(UDWgraph, file)
        importer.importGraph(DWgraphNew, file)

        assertEquals(DWgraphNew.vertexSet(), setOf(1, 2, 3))
        assertEquals(DWgraphNew.edgeSet(), setOf("A", "B", "C"))
        assertEquals(DWgraphNew.getEdge(1, 2), "A")
        assertEquals(DWgraphNew.getEdge(2, 1), "A")
        assertEquals(DWgraphNew.getEdge(2, 3), "B")
        assertEquals(DWgraphNew.getEdge(3, 2), "B")
        assertEquals(DWgraphNew.getEdge(1, 3), "C")
        assertEquals(DWgraphNew.getEdge(3, 1), "C")
        assertTrue(DWgraphNew.configuration.isUndirected()) // becomes undirected because of import type
        assertTrue(DWgraphNew.configuration.isWeighted())
        assertTrue(UDWgraph.configuration.isUndirected())
        assertTrue(UDWgraph.configuration.isWeighted())
    }

    @Test
    fun `unweighted graph cast undirected to directed`() {
        UDUWgraph.addVertex(1)
        UDUWgraph.addVertex(2)
        UDUWgraph.addVertex(3)

        UDUWgraph.addEdge(1, 2, "A")
        UDUWgraph.addEdge(1, 3, "C")
        UDUWgraph.addEdge(2, 3, "B")

        exporter.exportGraph(UDUWgraph, file)
        importer.importGraph(DUWgraphNew, file)

        assertEquals(DUWgraphNew.vertexSet(), setOf(1, 2, 3))
        assertEquals(DUWgraphNew.edgeSet(), setOf("A", "B", "C"))
        assertEquals(DUWgraphNew.getEdge(1, 2), "A")
        assertEquals(DUWgraphNew.getEdge(2, 1), "A")
        assertEquals(DUWgraphNew.getEdge(2, 3), "B")
        assertEquals(DUWgraphNew.getEdge(3, 2), "B")
        assertEquals(DUWgraphNew.getEdge(1, 3), "C")
        assertEquals(DUWgraphNew.getEdge(3, 1), "C")
        assertTrue(DUWgraphNew.configuration.isUndirected()) // becomes undirected because of import type
        assertTrue(DUWgraphNew.configuration.isUnweighted())
        assertTrue(UDUWgraph.configuration.isUndirected())
        assertTrue(UDUWgraph.configuration.isUnweighted())
    }
}
