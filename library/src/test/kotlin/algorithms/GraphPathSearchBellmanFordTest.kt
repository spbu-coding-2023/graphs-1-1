package algorithms

import display.cycleSearch.implementation.GraphVertexCycleSearchWithDfs
import display.pathSearch.implementation.GraphPathSearchBellmanFord
import graph.Graph
import graph.implementation.DirectedWeightedGraph
import graph.implementation.UndirectedWeightedGraph
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class GraphPathSearchBellmanFordTest {
    var DWgraph = DirectedWeightedGraph<Int, String>()
    var UDWgraph = UndirectedWeightedGraph<Int, String>()
    val bellmanFord = GraphPathSearchBellmanFord()

    @BeforeEach
    fun setup() {
        DWgraph = DirectedWeightedGraph()
        UDWgraph = UndirectedWeightedGraph()
    }

    fun setupGraph1(graph: Graph<Int, String>) {
        graph.addVertex(1)
        graph.addVertex(23)
        graph.addVertex(52)
        graph.addVertex(0)
        graph.addVertex(9)
        graph.addVertex(7)
        graph.addVertex(69)

        graph.addEdge(1, 23, "A", 1.0)
        graph.addEdge(9, 23, "B", .5)
        graph.addEdge(23, 52, "C", 1.0)
        graph.addEdge(52, 9, "D", 2.0)
        graph.addEdge(52, 69, "E", 1.0)
        graph.addEdge(52, 7, "F", .5)
        graph.addEdge(9, 7, "G", -3.0)
        graph.addEdge(9, 69, "H", 0.0)
    }

    @Test
    fun `search path from to self directed`() {
        setupGraph1(DWgraph)

        assertEquals(bellmanFord.searchPath(DWgraph, 9, 9), listOf(9))
    }

    @Test
    fun `search path from to self undirected`() {
        setupGraph1(UDWgraph)

        assertEquals(bellmanFord.searchPath(UDWgraph, 9, 9), listOf<Int>())
    }

    @Test
    fun `search path exists directed`() {
        setupGraph1(DWgraph)

        assertEquals(bellmanFord.searchPath(DWgraph, 1, 69), listOf(1, 23, 52, 69))
    }

    @Test
    fun `search path exists undirected`() {
        setupGraph1(UDWgraph)
        UDWgraph.setEdgeWeight("G", 1.0)

        assertEquals(bellmanFord.searchPath(UDWgraph, 1, 7), listOf(1, 23, 9, 7))
    }

    @Test
    fun `search path not exists directed`() {
        setupGraph1(DWgraph)

        assertEquals(bellmanFord.searchPath(DWgraph, 9, 1), listOf<Int>())
    }

    @Test
    fun `search path not exists undirected`() {
        setupGraph1(UDWgraph)
        UDWgraph.setEdgeWeight("G", 1.0)

        assertEquals(bellmanFord.searchPath(UDWgraph, 1, 0), listOf<Int>())
    }

    @Test
    fun `search path directed two vertices`() {
        DWgraph.addVertex(1)
        DWgraph.addVertex(2)
        DWgraph.addEdge(1, 2, "A")

        assertEquals(bellmanFord.searchPath(DWgraph, 1, 2), listOf(1, 2))
    }
}
