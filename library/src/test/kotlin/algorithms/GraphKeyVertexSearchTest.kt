package algorithms

import display.keyVertex.implementation.GraphBetweennessCentrality
import graph.Graph
import graph.implementation.UndirectedWeightedGraph
import graph.implementation.DirectedWeightedGraph
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach


class GraphKeyVertexSearchTestTest {
    var UDWgraph = UndirectedWeightedGraph<Int, String>()
    var DWgraph = DirectedWeightedGraph<Int, String>()
    val KeyVertexFinder = GraphBetweennessCentrality()

    @BeforeEach
    fun setup() {
        UDWgraph = UndirectedWeightedGraph()
        DWgraph = DirectedWeightedGraph()
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

    @Test
    fun testGraph1BetweennessCentrality() {
        setupGraph1(UDWgraph)
        val keyVertices = KeyVertexFinder.getKeyVertices(UDWgraph)

        assertEquals(4, keyVertices.size)
        assertEquals(0.0f, keyVertices[1])
        assertEquals(4.0f, keyVertices[2])
        assertEquals(0.0f, keyVertices[3])
        assertEquals(0.0f, keyVertices[4])
    }

    @Test
    fun testGraph2BetweennessCentrality() {
        setupGraph2(UDWgraph)
        val keyVertices = KeyVertexFinder.getKeyVertices(UDWgraph)

        assertEquals(6, keyVertices.size)
        assertEquals(0.0f, keyVertices[1])
        assertEquals(0.0f, keyVertices[2])
        assertEquals(13.0f, keyVertices[3])
        assertEquals(3.0f, keyVertices[4])
        assertEquals(3.0f, keyVertices[5])
        assertEquals(1.0f, keyVertices[6])
    }
}
