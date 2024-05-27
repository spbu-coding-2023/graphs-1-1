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
        assertNull(DWgraph.structure.matrix[1][0])
        assertTrue(DWgraph.containsEdge(23, 52))
        assertFalse(DWgraph.containsEdge(52, 23))
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
    }

    @Test
    fun `doesn't contain edge undirected`() {
        UDWgraph.addVertex(23)
        UDWgraph.addVertex(52)
        assertFalse(UDWgraph.containsEdge(23, 52))
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
        assertEquals(DWgraph.degreeOf(1), 2)
        assertEquals(DWgraph.degreeOf(9), 2)
        assertEquals(DWgraph.degreeOf(0), 0)
    }

    @Test
    fun `in degree of directed`() {
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
        assertEquals(DWgraph.inDegreeOf(1), 0)
        assertEquals(DWgraph.inDegreeOf(9), 2)
        assertEquals(DWgraph.inDegreeOf(0), 0)
    }

    @Test
    fun `in degree of undirected`() {
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

        assertEquals(UDWgraph.inDegreeOf(23), 1)
        assertEquals(UDWgraph.inDegreeOf(52), 3)
        assertEquals(UDWgraph.inDegreeOf(1), 2)
        assertEquals(UDWgraph.inDegreeOf(9), 2)
        assertEquals(UDWgraph.inDegreeOf(0), 0)
    }

    @Test
    fun `out degree of directed`() {
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
        assertEquals(DWgraph.outDegreeOf(9), 0)
        assertEquals(DWgraph.outDegreeOf(0), 0)
    }

    @Test
    fun `out degree of undirected`() {
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

        assertEquals(UDWgraph.outDegreeOf(23), 1)
        assertEquals(UDWgraph.outDegreeOf(52), 3)
        assertEquals(UDWgraph.outDegreeOf(1), 2)
        assertEquals(UDWgraph.outDegreeOf(9), 2)
        assertEquals(UDWgraph.outDegreeOf(0), 0)
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

        assertEquals(DWgraph.edgeSet(), setOf("A", "B", "C", "E"))
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
            setOf(Pair(23, 52), Pair(1, 52), Pair(52, 9), Pair(1, 9))
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
//        assertEquals(DWgraph.getEdgeHead("D"), null)
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
//        assertEquals(DWgraph.getEdgeTail("D"), null)
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

        assertEquals(DUWgraph.structure.matrix[0][1]?.weight, 2.0)
        assertEquals(DUWgraph.getEdgeWeight("A"), 1.0)
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

        val res = DWgraph.removeVertex(52)

        assertEquals(DWgraph.edgeSet(), setOf("E"))
        assertEquals(DWgraph.vertexSet(), setOf(23, 9, 1, 0))
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

        val res = DWgraph.removeEdge(52, 9)

        assertEquals(DWgraph.edgeSet(), setOf("A", "B", "E"))
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
    fun `incoming edges of directed graph`() {
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
    fun `incoming edges of undirected graph`() {
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

        assertEquals(UDWgraph.incomingEdgesOf(0), setOf<Int>())
        assertEquals(UDWgraph.incomingEdgesOf(23), setOf("A"))
        assertEquals(UDWgraph.incomingEdgesOf(52), setOf("A", "B", "C"))
        assertEquals(UDWgraph.incomingEdgesOf(1), setOf("B", "E")) // E is the same edge as D
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
    fun `vertices of`() {
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

        assertEquals(UDWgraph.verticesOf(52), setOf(23, 1, 9))
        assertEquals(UDWgraph.verticesOf(23), setOf(52))
        assertEquals(UDWgraph.verticesOf(1), setOf(52, 9))
        assertEquals(UDWgraph.verticesOf(0), setOf<Int>())
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

        assertEquals(DWgraph.edgesOf(1), setOf("B", "E"))
    }

    @Test
    fun `remove edge from an undirected graph head-tail`() {
        UDWgraph.addVertex(23)
        UDWgraph.addVertex(52)
        UDWgraph.addEdge(23, 52, "A", 3.0)
        UDWgraph.removeEdge(23, 52)

        assertEquals(setOf<Pair<Int,Int>>(), UDWgraph.edgeSetOfVertices())
        assertEquals(setOf(23, 52), UDWgraph.vertexSet())
    }

    @Test
    fun `remove edge from an undirected graph tail-head`() {
        UDWgraph.addVertex(23)
        UDWgraph.addVertex(52)
        UDWgraph.addEdge(23, 52, "A", 3.0)
        UDWgraph.removeEdge(52, 23)

        assertEquals(setOf<Pair<Int,Int>>(), UDWgraph.edgeSetOfVertices())
        assertEquals(setOf(52, 23), UDWgraph.vertexSet())
    }

    @Test
    fun `remove edge that doesn't exist in an undirected graph`() {
        UDWgraph.addVertex(1)
        UDWgraph.addVertex(1000)
        assertEquals(false, UDWgraph.removeEdge(1, 1000))
    }

    @Test
    fun `set edge weight unweighted graph`() {
        UDUWgraph.addVertex(23)
        UDUWgraph.addVertex(52)
        UDUWgraph.addEdge(23, 52, "A", 3.0)
        assertEquals(UDWgraph.DEFAULT_EDGE_WEIGHT, UDUWgraph.getEdgeWeight(23, 52))
        UDUWgraph.setEdgeWeight(23, 52, 5.0)
        assertEquals(UDUWgraph.DEFAULT_EDGE_WEIGHT, UDUWgraph.getEdgeWeight(23, 52))
    }

    @Test
    fun `set edge weight weighted graph`() {
        UDWgraph.addVertex(23)
        UDWgraph.addVertex(52)
        UDWgraph.addEdge(23, 52, "A", 3.0)
        assertEquals(3.0, UDWgraph.getEdgeWeight(23, 52))
        UDWgraph.setEdgeWeight(23, 52, 5.0)
        assertEquals(5.0, UDWgraph.getEdgeWeight(23, 52))
    }

    @Test
    fun `should throw RuntimeException since edge doesn't exist function getEdgeWeight(e)`() {
        assertThrows(RuntimeException::class.java){
            UDWgraph.getEdgeWeight("A")
        }
    }

    @Test
    fun `should throw RuntimeException since edge doesn't exist function getEdgeWeight(v, v)`() {
        DWgraph.addVertex(1)
        DWgraph.addVertex(1000)
        assertThrows(RuntimeException::class.java){
            DWgraph.getEdgeWeight(1, 1000)
        }
    }

    @Test
    fun `should throw Error since there is no edge function getEdgeHead(e)`() {
        assertThrows(Error::class.java) {
            DWgraph.getEdgeHead("A")
        }
    }

    @Test
    fun `should throw Error since there is no edge function getEdgeTail(e)`() {
        assertThrows(Error::class.java) {
            DWgraph.getEdgeTail("A")
        }
    }

    @Test
    fun `get edge directed but edge doesn't exist between vertices that exist`() {
        DWgraph.addVertex(1)
        DWgraph.addVertex(1000)
        assertEquals(null, DWgraph.getEdge(1, 1000))
    }

    @Test
    fun `get edge undirected access by tail-head but edge doesn't exist between vertices that exist`() {
        UDWgraph.addVertex(1)
        UDWgraph.addVertex(1000)
        assertEquals(null, UDWgraph.getEdge(1, 1000))
    }

    @Test
    fun `get edge undirected access by head-tail but edge doesn't exist between vertices that exist`() {
        UDWgraph.addVertex(1)
        UDWgraph.addVertex(1000)
        assertEquals(null, UDWgraph.getEdge(1000, 1))
    }

    @Test
    fun `weird setEdgeWeight 1`() {
        val DWgraphWithNull = DirectedWeightedGraph<Int?, String?>()
        DWgraphWithNull.addVertex(1)
        DWgraphWithNull.addVertex(1000)
        DWgraphWithNull.setEdgeWeight(1, 1000, 4.0)
        DWgraphWithNull.addEdge(1, 1000, null, 3.0)
        assertEquals(true, DWgraphWithNull.containsVertex(1))
        assertEquals(true, DWgraphWithNull.containsVertex(1000))
        assertEquals(true, DWgraphWithNull.containsEdge(1, 1000))
        assertEquals(3.0, DWgraphWithNull.getEdgeWeight(null))
        DWgraphWithNull.setEdgeWeight(null, 4.0)
        assertEquals(true, DWgraphWithNull.containsVertex(1))
        assertEquals(true, DWgraphWithNull.containsVertex(1000))
        assertEquals(true, DWgraphWithNull.containsEdge(1, 1000))
        assertEquals(4.0, DWgraphWithNull.getEdgeWeight(null))
    }

    @Test
    fun `weird setEdgeWeight 2`() {
        val DWgraphWithNull = DirectedWeightedGraph<Int?, String?>()
        DWgraphWithNull.addVertex(1)
        DWgraphWithNull.addVertex(1000)
        DWgraphWithNull.setEdgeWeight(1, 1000, 4.0)
        DWgraphWithNull.addEdge(1, 1000, null, 3.0)
        assertEquals(true, DWgraphWithNull.containsVertex(1))
        assertEquals(true, DWgraphWithNull.containsVertex(1000))
        assertEquals(true, DWgraphWithNull.containsEdge(1, 1000))
        assertEquals(3.0, DWgraphWithNull.getEdgeWeight(null))
        DWgraphWithNull.setEdgeWeight(1, 1000, 4.0)
        assertEquals(true, DWgraphWithNull.containsVertex(1))
        assertEquals(true, DWgraphWithNull.containsVertex(1000))
        assertEquals(true, DWgraphWithNull.containsEdge(1, 1000))
        assertEquals(4.0, DWgraphWithNull.getEdgeWeight(null))
    }

    @Test
    fun `remove imaginary vertex`() {
        assertEquals(false, DWgraph.removeVertex(1))
    }
}
