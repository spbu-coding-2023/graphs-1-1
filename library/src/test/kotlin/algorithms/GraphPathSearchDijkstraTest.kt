package algorithms

import display.pathSearch.implementation.GraphDijkstraPathFinder
import graph.Graph
import graph.implementation.DirectedWeightedGraph
import graph.implementation.UndirectedWeightedGraph
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class GraphPathSearchDijkstraTest {
    var DWgraph = DirectedWeightedGraph<Int, String>()
    var UDWgraph = UndirectedWeightedGraph<Int, String>()
    val Dijkstra = GraphDijkstraPathFinder()

    @BeforeEach
    fun setup() {
        DWgraph = DirectedWeightedGraph()
        UDWgraph = UndirectedWeightedGraph()
    }

    fun setupGraph1(graph: Graph<Int, String>) {
        graph.addVertex(1)
        graph.addVertex(2)
        graph.addVertex(3)
        graph.addVertex(4)
        graph.addVertex(5)
        graph.addVertex(6)
        graph.addVertex(7)

        graph.addEdge(1, 2, "A")
        graph.addEdge(5, 2, "B")
        graph.addEdge(2, 3, "C")
        graph.addEdge(3, 5, "D")
        graph.addEdge(3, 7, "E")
        graph.addEdge(3, 6, "F")
        graph.addEdge(5, 6, "G")
        graph.addEdge(5, 7, "H")

        graph.setEdgeWeight("A", 1.0)
        graph.setEdgeWeight("B", 1.5)
        graph.setEdgeWeight("C", 2.0)
        graph.setEdgeWeight("D", 2.5)
        graph.setEdgeWeight("E", 1.0)
        graph.setEdgeWeight("F", 1.5)
        graph.setEdgeWeight("G", 2.0)
        graph.setEdgeWeight("H", 3.0)
    }

    @Test
    fun `search path from to self directed`() {
        setupGraph1(DWgraph)
        assertEquals(Dijkstra.searchPath(DWgraph, 5, 5), mutableListOf(5))
    }

    @Test
    fun `search path from to self undirected`() {
        setupGraph1(UDWgraph)
        assertEquals(Dijkstra.searchPath(UDWgraph, 5, 5), mutableListOf(5))
    }

    @Test
    fun `search path exists directed`() {
        setupGraph1(DWgraph)
        assertEquals(Dijkstra.searchPath(DWgraph, 1, 7), mutableListOf(1, 2, 3, 7))
    }

    @Test
    fun `search path exists undirected`() {
        setupGraph1(UDWgraph)
        assertEquals(Dijkstra.searchPath(UDWgraph, 1, 6), mutableListOf(1, 2, 5, 6))
    }

    @Test
    fun `search path not exists directed`() {
        setupGraph1(DWgraph)
        assertEquals(Dijkstra.searchPath(DWgraph, 5, 1), mutableListOf<Int>())
    }

    @Test
    fun `search path not exists undirected`() {
        setupGraph1(UDWgraph)
        assertEquals(Dijkstra.searchPath(UDWgraph, 1, 4), mutableListOf<Int>())
    }
}
