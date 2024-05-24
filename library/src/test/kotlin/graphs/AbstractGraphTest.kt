package graphs

import graph.implementation.DirectedUnweightedGraph
import graph.implementation.DirectedWeightedGraph
import graph.implementation.UndirectedUnweightedGraph
import graph.implementation.UndirectedWeightedGraph
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class AbstractGraphTest {
    var DWgraph = DirectedWeightedGraph<Int, String>()
    var UDWgraph = UndirectedWeightedGraph<Int, String>()
    var DUWgraph = DirectedUnweightedGraph<Int, String>()
    var UDUWgraph = UndirectedUnweightedGraph<Int, String>()
    @BeforeEach
    fun setup() {
        DWgraph = DirectedWeightedGraph()
        UDWgraph = UndirectedWeightedGraph()
        DUWgraph = DirectedUnweightedGraph()
        UDUWgraph = UndirectedUnweightedGraph()
    }

    @Test
    fun `get configuration`() {
        assertTrue(DWgraph.configuration.isDirected())
        assertTrue(DWgraph.configuration.isWeighted())
        assertTrue(UDWgraph.configuration.isUndirected())
        assertTrue(UDWgraph.configuration.isWeighted())
        assertTrue(DUWgraph.configuration.isDirected())
        assertTrue(DUWgraph.configuration.isUnweighted())
        assertTrue(UDUWgraph.configuration.isUndirected())
        assertTrue(UDUWgraph.configuration.isUnweighted())
    }

    @Test
    fun `add one vertex`() {
        DWgraph.addVertex(23)

        assertEquals(DWgraph.structure.verticesMap[23], 0)
        assertEquals(DWgraph.structure.matrix[0][0], null)
        assertEquals(DWgraph.getNOfVertices(), 1)
    }

    @Test
    fun `add two vertices`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)

        assertEquals(DWgraph.structure.verticesMap[23], 0)
        assertEquals(DWgraph.structure.verticesMap[52], 1)
        assertEquals(DWgraph.structure.matrix[0][0], null)
        assertEquals(DWgraph.structure.matrix[0][1], null)
        assertEquals(DWgraph.structure.matrix[1][0], null)
        assertEquals(DWgraph.structure.matrix[1][1], null)
        assertEquals(DWgraph.getNOfVertices(), 2)
    }

    @Test
    fun `add one edge directed weighted`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addEdge(23, 52, "A")

        assertEquals(DWgraph.structure.matrix[0][1]?.weight, 1.0)
        assertEquals(DWgraph.structure.matrix[0][1]?.data, "A")
        assertEquals(DWgraph.structure.matrix[0][0]?.data, null)
        assertEquals(DWgraph.structure.matrix[1][0]?.data, null)
        assertEquals(DWgraph.structure.matrix[1][1]?.data, null)
        assertEquals(DWgraph.getNOfEdges(), 1)
        assertEquals(DWgraph.getNOfVertices(), 2)
    }

    @Test
    fun `add one edge undirected weighted`() {
        UDWgraph.addVertex(23)
        UDWgraph.addVertex(52)
        UDWgraph.addEdge(23, 52, "A")

        assertEquals(UDWgraph.structure.matrix[0][1]?.weight, 1.0)
        assertEquals(UDWgraph.structure.matrix[1][0]?.weight, null)
        assertEquals(UDWgraph.structure.matrix[0][1]?.data, "A")
        assertEquals(UDWgraph.structure.matrix[1][0]?.data, null)
        assertEquals(UDWgraph.structure.matrix[0][0]?.data, null)
        assertEquals(UDWgraph.structure.matrix[1][1]?.data, null)
        assertEquals(UDWgraph.getNOfEdges(), 1)
        assertEquals(UDWgraph.getNOfVertices(), 2)
    }

    @Test
    fun `contains vertex`() {
        DWgraph.addVertex(23)

        assertNotNull(DWgraph.structure.verticesMap[23])
        assertTrue(DWgraph.containsVertex(23))
    }

    @Test
    fun `contains edge directed`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addEdge(23, 52, "A")

        assertNotNull(DWgraph.structure.matrix[0][1])
        assertTrue(DWgraph.containsEdge(23, 52))
        assertEquals(DWgraph.getNOfEdges(), 1)
        assertEquals(DWgraph.getNOfVertices(), 2)
    }

    @Test
    fun `contains edge undirected`() {
        UDWgraph.addVertex(23)
        UDWgraph.addVertex(52)
        UDWgraph.addEdge(23, 52, "A")

        assertNotNull(UDWgraph.structure.matrix[0][1])
        assertNull(UDWgraph.structure.matrix[1][0])
        assertTrue(UDWgraph.containsEdge(23, 52))
        assertTrue(UDWgraph.containsEdge(52, 23))
        assertEquals(UDWgraph.getNOfEdges(), 1)
        assertEquals(UDWgraph.getNOfVertices(), 2)
    }

    @Test
    fun `degree of`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addVertex(1)
        DWgraph.addVertex(9)
        DWgraph.addVertex(0)
        DWgraph.addEdge(23, 52, "A")
        DWgraph.addEdge(1, 52, "B")
        DWgraph.addEdge(52, 9, "C")
        DWgraph.addEdge(9, 1, "D")
        DWgraph.addEdge(1, 9, "E")

        assertEquals(DWgraph.degreeOf(23), 1)
        assertEquals(DWgraph.degreeOf(52), 3)
        assertEquals(DWgraph.degreeOf(1), 3)
        assertEquals(DWgraph.degreeOf(9), 3)
        assertEquals(DWgraph.degreeOf(0), 0)
        assertEquals(DWgraph.getNOfEdges(), 5)
        assertEquals(DWgraph.getNOfVertices(), 5)
    }

    @Test
    fun `in degree of`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addVertex(1)
        DWgraph.addVertex(9)
        DWgraph.addVertex(0)
        DWgraph.addEdge(23, 52, "A")
        DWgraph.addEdge(1, 52, "B")
        DWgraph.addEdge(52, 9, "C")
        DWgraph.addEdge(9, 1, "D")
        DWgraph.addEdge(1, 9, "E")

        assertEquals(DWgraph.inDegreeOf(23), 0)
        assertEquals(DWgraph.inDegreeOf(52), 2)
        assertEquals(DWgraph.inDegreeOf(1), 1)
        assertEquals(DWgraph.inDegreeOf(9), 2)
        assertEquals(DWgraph.inDegreeOf(0), 0)
        assertEquals(DWgraph.getNOfEdges(), 5)
        assertEquals(DWgraph.getNOfVertices(), 5)
    }

    @Test
    fun `out degree of`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addVertex(1)
        DWgraph.addVertex(9)
        DWgraph.addVertex(0)
        DWgraph.addEdge(23, 52, "A")
        DWgraph.addEdge(1, 52, "B")
        DWgraph.addEdge(52, 9, "C")
        DWgraph.addEdge(9, 1, "D")
        DWgraph.addEdge(1, 9, "E")

        assertEquals(DWgraph.outDegreeOf(23), 1)
        assertEquals(DWgraph.outDegreeOf(52), 1)
        assertEquals(DWgraph.outDegreeOf(1), 2)
        assertEquals(DWgraph.outDegreeOf(9), 1)
        assertEquals(DWgraph.outDegreeOf(0), 0)
        assertEquals(DWgraph.getNOfEdges(), 5)
        assertEquals(DWgraph.getNOfVertices(), 5)
    }

    @Test
    fun `edge set`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addVertex(1)
        DWgraph.addVertex(9)
        DWgraph.addVertex(0)
        DWgraph.addEdge(23, 52, "A")
        DWgraph.addEdge(1, 52, "B")
        DWgraph.addEdge(52, 9, "C")
        DWgraph.addEdge(9, 1, "D")
        DWgraph.addEdge(1, 9, "E")

        assertEquals(DWgraph.edgeSet(), setOf("A", "B", "C", "D", "E"))
    }

    @Test
    fun `edge set of vertices directed`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addVertex(1)
        DWgraph.addVertex(9)
        DWgraph.addVertex(0)
        DWgraph.addEdge(23, 52, "A")
        DWgraph.addEdge(1, 52, "B")
        DWgraph.addEdge(52, 9, "C")
        DWgraph.addEdge(9, 1, "D")
        DWgraph.addEdge(1, 9, "E")

        assertEquals(
            DWgraph.edgeSetOfVertices(),
            setOf(Pair(23, 52), Pair(1, 52), Pair(52, 9), Pair(9, 1), Pair(1, 9))
        )
    }

    @Test
    fun `edge set of vertices undirected`() {
        UDWgraph.addVertex(23)
        UDWgraph.addVertex(52)
        UDWgraph.addVertex(1)
        UDWgraph.addVertex(9)
        UDWgraph.addVertex(0)
        UDWgraph.addEdge(23, 52, "A")
        UDWgraph.addEdge(1, 52, "B")
        UDWgraph.addEdge(52, 9, "C")
        UDWgraph.addEdge(9, 1, "D")
        UDWgraph.addEdge(1, 9, "E")

        assertEquals(
            UDWgraph.edgeSetOfVertices(),
            setOf(
                Pair(23, 52), Pair(52, 23), Pair(1, 52), Pair(52, 1),
                Pair(52, 9), Pair(9, 52), Pair(9, 1), Pair(1, 9)
            )
        )
    }

    @Test
    fun `vertex set`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addVertex(1)
        DWgraph.addVertex(9)
        DWgraph.addVertex(0)
        DWgraph.addEdge(23, 52, "A")
        DWgraph.addEdge(1, 52, "B")
        DWgraph.addEdge(52, 9, "C")
        DWgraph.addEdge(9, 1, "D")
        DWgraph.addEdge(1, 9, "E")

        assertEquals(DWgraph.vertexSet(), setOf(23, 52, 1, 9, 0))
    }

    @Test
    fun `get edge head`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addVertex(1)
        DWgraph.addVertex(9)
        DWgraph.addVertex(0)
        DWgraph.addEdge(23, 52, "A")
        DWgraph.addEdge(1, 52, "B")
        DWgraph.addEdge(52, 9, "C")
        DWgraph.addEdge(9, 1, "D")
        DWgraph.addEdge(1, 9, "E")

        assertEquals(DWgraph.getEdgeHead("A"), 52)
        assertEquals(DWgraph.getEdgeHead("B"), 52)
        assertEquals(DWgraph.getEdgeHead("C"), 9)
        assertEquals(DWgraph.getEdgeHead("D"), 1)
        assertEquals(DWgraph.getEdgeHead("E"), 9)
    }

    @Test
    fun `get edge tail`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addVertex(1)
        DWgraph.addVertex(9)
        DWgraph.addVertex(0)
        DWgraph.addEdge(23, 52, "A")
        DWgraph.addEdge(1, 52, "B")
        DWgraph.addEdge(52, 9, "C")
        DWgraph.addEdge(9, 1, "D")
        DWgraph.addEdge(1, 9, "E")

        assertEquals(DWgraph.getEdgeTail("A"), 23)
        assertEquals(DWgraph.getEdgeTail("B"), 1)
        assertEquals(DWgraph.getEdgeTail("C"), 52)
        assertEquals(DWgraph.getEdgeTail("D"), 9)
        assertEquals(DWgraph.getEdgeTail("E"), 1)
    }

    @Test
    fun `set edge weight weighted`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addEdge(23, 52, "A")

        DWgraph.setEdgeWeight("A", 2.0)

        assertEquals(DWgraph.structure.matrix[0][1]?.weight, 2.0)
    }

    @Test
    fun `set edge weight unweighted`() {
        DUWgraph.addVertex(23)
        DUWgraph.addVertex(52)
        DUWgraph.addEdge(23, 52, "A")

        DUWgraph.setEdgeWeight("A", 2.0)

        assertEquals(DUWgraph.structure.matrix[0][1]?.weight, 1.0)
    }

    @Test
    fun `remove vertex`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addVertex(1)
        DWgraph.addVertex(9)
        DWgraph.addVertex(0)
        DWgraph.addEdge(23, 52, "A")
        DWgraph.addEdge(1, 52, "B")
        DWgraph.addEdge(52, 9, "C")
        DWgraph.addEdge(9, 1, "D")
        DWgraph.addEdge(1, 9, "E")
        assertEquals(DWgraph.getNOfEdges(), 5)
        assertEquals(DWgraph.getNOfVertices(), 5)

        val res = DWgraph.removeVertex(52)

        assertEquals(DWgraph.edgeSet(), setOf("D", "E"))
        assertEquals(DWgraph.vertexSet(), setOf(23, 9, 1, 0))
        assertEquals(DWgraph.getNOfEdges(), 2)
        assertEquals(DWgraph.getNOfVertices(), 4)
        assertTrue(res)
    }

    @Test
    fun `remove edge`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addVertex(1)
        DWgraph.addVertex(9)
        DWgraph.addVertex(0)
        DWgraph.addEdge(23, 52, "A")
        DWgraph.addEdge(1, 52, "B")
        DWgraph.addEdge(52, 9, "C")
        DWgraph.addEdge(9, 1, "D")
        DWgraph.addEdge(1, 9, "E")
        assertEquals(DWgraph.getNOfEdges(), 5)
        assertEquals(DWgraph.getNOfVertices(), 5)

        val res = DWgraph.removeEdge(52, 9)

        assertEquals(DWgraph.edgeSet(), setOf("A", "B", "D", "E"))
        assertEquals(DWgraph.getNOfEdges(), 4)
        assertEquals(DWgraph.getNOfVertices(), 5)
        assertTrue(res)
    }

    @Test
    fun `outgoing edges of`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addVertex(1)
        DWgraph.addVertex(9)
        DWgraph.addVertex(0)
        DWgraph.addEdge(23, 52, "A")
        DWgraph.addEdge(1, 52, "B")
        DWgraph.addEdge(52, 9, "C")
        /* DWgraph.addEdge(9, 1, "D") */
        DWgraph.addEdge(1, 9, "E")

        assertEquals(DWgraph.outgoingEdgesOf(1), setOf("B", "E"))
        assertEquals(DWgraph.outgoingEdgesOf(0), setOf<Int>())
        assertEquals(DWgraph.outgoingEdgesOf(9), setOf<Int>())
    }

    @Test
    fun `incoming edges of`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addVertex(1)
        DWgraph.addVertex(9)
        DWgraph.addVertex(0)
        DWgraph.addEdge(23, 52, "A")
        DWgraph.addEdge(1, 52, "B")
        DWgraph.addEdge(52, 9, "C")
        DWgraph.addEdge(9, 1, "D")
        DWgraph.addEdge(1, 9, "E")

        assertEquals(DWgraph.incomingEdgesOf(52), setOf("A", "B"))
        assertEquals(DWgraph.incomingEdgesOf(0), setOf<Int>())
        assertEquals(DWgraph.incomingEdgesOf(23), setOf<Int>())
    }

    @Test
    fun `outgoing vertices of`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addVertex(1)
        DWgraph.addVertex(9)
        DWgraph.addVertex(0)
        DWgraph.addEdge(23, 52, "A")
        DWgraph.addEdge(1, 52, "B")
        DWgraph.addEdge(52, 9, "C")
        DWgraph.addEdge(9, 1, "D")
        DWgraph.addEdge(1, 9, "E")

        assertEquals(DWgraph.outgoingVerticesOf(1), setOf(52, 9))
        assertEquals(DWgraph.outgoingVerticesOf(0), setOf<Int>())
    }

    @Test
    fun `incoming vertices of`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addVertex(1)
        DWgraph.addVertex(9)
        DWgraph.addVertex(0)
        DWgraph.addEdge(23, 52, "A")
        DWgraph.addEdge(1, 52, "B")
        DWgraph.addEdge(52, 9, "C")
        DWgraph.addEdge(9, 1, "D")
        DWgraph.addEdge(1, 9, "E")

        assertEquals(DWgraph.incomingVerticesOf(52), setOf(23, 1))
        assertEquals(DWgraph.incomingVerticesOf(0), setOf<Int>())
    }

    @Test
    fun `get edge weight`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addEdge(23, 52, "A")

        assertEquals(DWgraph.getEdgeWeight("A"), 1.0)
    }

    @Test
    fun `get edge directed`() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addEdge(23, 52, "A")

        assertEquals(DWgraph.getEdge(23, 52), "A")
    }

    @Test
    fun `get edge undirected`() {
        UDWgraph.addVertex(23)
        UDWgraph.addVertex(52)
        UDWgraph.addEdge(23, 52, "A")

        assertEquals(UDWgraph.getEdge(23, 52), "A")
        assertEquals(UDWgraph.getEdge(52, 23), "A")
    }

    @Test
    fun edgesOf() {
        DWgraph.addVertex(23)
        DWgraph.addVertex(52)
        DWgraph.addVertex(1)
        DWgraph.addVertex(9)
        DWgraph.addVertex(0)
        DWgraph.addEdge(23, 52, "A")
        DWgraph.addEdge(1, 52, "B")
        DWgraph.addEdge(52, 9, "C")
        DWgraph.addEdge(9, 1, "D")
        DWgraph.addEdge(1, 9, "E")

        assertEquals(DWgraph.edgesOf(1), setOf("B", "D", "E"))
    }
}
