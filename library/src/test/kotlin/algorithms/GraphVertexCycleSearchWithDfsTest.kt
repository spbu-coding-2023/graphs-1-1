package algorithms

import display.cycleSearch.implementation.GraphVertexCycleSearchWithDfs
import graph.Graph
import graph.implementation.DirectedWeightedGraph
import graph.implementation.UndirectedWeightedGraph
import org.hamcrest.CoreMatchers.anyOf
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.`is`

class GraphVertexCycleSearchWithDfsTest {
    var DWgraph = DirectedWeightedGraph<Int, String>()
    var UDWgraph = UndirectedWeightedGraph<Int, String>()
    val dfsCycle = GraphVertexCycleSearchWithDfs()

    @BeforeEach
    fun setup() {
       DWgraph = DirectedWeightedGraph()
       UDWgraph = UndirectedWeightedGraph()
    }

    fun setupCycle1(graph: Graph<Int, String>) {
        graph.addVertex(1)
        graph.addVertex(9)
        graph.addVertex(23)
        graph.addVertex(52)
        graph.addVertex(0)

        graph.addEdge(1, 23, "A")
        graph.addEdge(23, 52, "B")
        graph.addEdge(9, 23, "C")
        graph.addEdge(52, 0, "D")
        graph.addEdge(0, 23, "E")
        graph.addEdge(0, 9, "F")
    }

    @Test
    fun `get cycle true directed`() {
        setupCycle1(DWgraph)

        val cycle = dfsCycle.getCycle(DWgraph, 23)

        assertNotNull(cycle)
        assertThat(cycle, anyOf(`is`(listOf(23, 52, 0, 23)), `is`(listOf(23, 52, 0, 9, 23))))
    }

    @Test
    fun `get cycle true undirected`() {
        setupCycle1(UDWgraph)
        val cycle = dfsCycle.getCycle(UDWgraph, 0)

        assertNotNull(cycle)
        val c1 = listOf(0, 9, 23, 0)
        val c2 = listOf(0, 9, 23, 52, 0)
        val c3 = listOf(0, 23, 52, 0)

        assertThat(cycle, anyOf(
            `is`(c1),
            `is`(c2),
            `is`(c3),
            `is`(c1.reversed()),
            `is`(c2.reversed()),
            `is`(c3.reversed()),
        ))
        println(cycle)

    }

    @Test
    fun `get cycle false directed`() {
        setupCycle1(DWgraph)
        val cycle = dfsCycle.getCycle(DWgraph, 1)

        assertEquals(cycle, listOf<Int>())
    }

    @Test
    fun `get cycle false undirected`() {
        setupCycle1(UDWgraph)
        val cycle = dfsCycle.getCycle(UDWgraph, 1)

        assertEquals(cycle, listOf<Int>())
    }
}