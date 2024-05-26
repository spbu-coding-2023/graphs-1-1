package algorithms

import display.bridgeSearch.implementation.GraphTarjanBridgeFinder
import graph.Graph
import graph.implementation.UndirectedWeightedGraph
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach


class GraphTarjanBridgeFinderTest {
    var UDWgraph = UndirectedWeightedGraph<Int, String>()
    val bridgeFinder = GraphTarjanBridgeFinder()

    @BeforeEach
    fun setup() {
        UDWgraph = UndirectedWeightedGraph()
    }

    fun setupGraph1(graph: Graph<Int, String>) {
        graph.addVertex(1)
        graph.addVertex(2)
        graph.addVertex(3)
        graph.addVertex(4)

        graph.addEdge(1, 2, "A")
        graph.addEdge(2, 3, "B")
        graph.addEdge(1, 3, "C")
        graph.addEdge(2, 4, "D")

    }

    fun setupGraph2(graph: Graph<Int, String>) {
        graph.addVertex(1)
        graph.addVertex(2)
        graph.addVertex(3)
        graph.addVertex(4)
        graph.addVertex(5)
        graph.addVertex(6)

        graph.addEdge(1, 2, "A")
        graph.addEdge(2, 3, "B")
        graph.addEdge(1, 3, "C")
        graph.addEdge(3, 4, "D")
        graph.addEdge(3, 5, "E")
        graph.addEdge(4, 6, "F")
        graph.addEdge(5, 6, "G")
    }

    fun setupGraph3(graph: Graph<Int, String>) {
        graph.addVertex(1)
        graph.addVertex(2)
        graph.addVertex(3)
        graph.addVertex(4)
        graph.addVertex(5)
        graph.addVertex(6)

        graph.addEdge(1, 2, "A")
        graph.addEdge(2, 4, "B")
        graph.addEdge(2, 3, "C")
        graph.addEdge(4, 5, "D")
        graph.addEdge(2, 5, "E")
    }


    @Test
    fun `all edges are bridges in undirected graph`() {
        setupGraph1(UDWgraph)
        val bridges = bridgeFinder.getBridges(UDWgraph)
        assertEquals(bridges.sortedBy { it.first }, listOf(Pair(1, 2), Pair(2, 3), Pair(2, 4)))
    }

    @Test
    fun `find bridges in undirected graph 1`() {
        setupGraph3(UDWgraph)
        val bridges = bridgeFinder.getBridges(UDWgraph)
        assertEquals(bridges.sortedBy { it.first }, listOf(Pair(1, 2), Pair(2, 3), Pair(2, 4), Pair(4, 5)))
    }

    @Test
    fun `find bridges in undirected graph 2`() {
        setupGraph2(UDWgraph)
        val bridges = bridgeFinder.getBridges(UDWgraph)
        assertEquals(bridges.sortedBy { it.first }, listOf(Pair(1, 2), Pair(2, 3), Pair(3, 4), Pair(3, 5), Pair(4, 6)))
    }
}
