package repository

import graph.implementation.DirectedWeightedGraph
import graph.implementation.UndirectedWeightedGraph
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import repository.implementation.GraphJSONExporter
import repository.implementation.GraphJSONImporter
import java.io.File

class GraphJSONImportExport {
    var DWgraph = DirectedWeightedGraph<Int, String>()
    var UDWgraph = UndirectedWeightedGraph<Int, String>()
    var DWgraphNew = DirectedWeightedGraph<Int, String>()
    var UDWgraphNew = UndirectedWeightedGraph<Int, String>()
    var file = File.createTempFile("jsonGraph", "json")
    val exporter = GraphJSONExporter()
    val importer = GraphJSONImporter()

    @BeforeEach
    fun setup() {
        DWgraph = DirectedWeightedGraph()
        DWgraphNew = DirectedWeightedGraph()
        UDWgraph = UndirectedWeightedGraph()
        UDWgraphNew = UndirectedWeightedGraph()
        file = File.createTempFile("jsonGraph", ".json")

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
    fun `cast undirected to directed`() {
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
}
