package algorithms

import display.stronglyConnectedComponentSearch.GraphSCCSearch
import display.stronglyConnectedComponentSearch.implementation.GraphSCCSearchWithTarjan
import graph.Graph
import graph.implementation.DirectedWeightedGraph
import graph.implementation.DirectedUnweightedGraph
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class GraphStronglyConnectedComponentTest {
    val SCCsearch = GraphSCCSearchWithTarjan<String, Int>()
    private fun setupGraph1(graph: Graph<String, Int>): Graph<String, Int> {
        graph.addVertex("a")
        graph.addVertex("b")
        graph.addVertex("c")
        graph.addVertex("d")
        graph.addVertex("e")
        graph.addVertex("f")
        graph.addVertex("g")
        graph.addVertex("h")

        graph.addEdge("a", "b", 1)
        graph.addEdge("b", "e", 2)
        graph.addEdge("e", "a", 3)
        graph.addEdge("b", "f", 4)
        graph.addEdge("e", "f", 5)
        graph.addEdge("f", "g", 6)
        graph.addEdge("g", "f", 7)
        graph.addEdge("b", "c", 8)
        graph.addEdge("c", "g", 9)
        graph.addEdge("c", "d", 10)
        graph.addEdge("d", "c", 11)
        graph.addEdge("d", "h", 12)
        graph.addEdge("h", "d", 13)
        graph.addEdge("h", "g", 14)

        return graph
    }

    private fun setupGraph2(graph: Graph<String, Int>): Graph<String, Int> {
        val newGraph = setupGraph1(graph)
        newGraph.addVertex("k")
        return newGraph
    }

    private fun setupGraph3(graph: Graph<String, Int>): Graph<String, Int> {
        graph.addVertex("a")
        graph.addVertex("b")
        graph.addVertex("c")
        graph.addVertex("d")
        graph.addVertex("e")
        graph.addVertex("f")
        graph.addVertex("g")
        graph.addVertex("h")
        graph.addVertex("i")
        graph.addVertex("j")
        graph.addVertex("k")
        graph.addVertex("l")
        graph.addVertex("m")
        graph.addVertex("n")

        graph.addEdge("a", "b", 1)
        graph.addEdge("c", "b", 2)
        graph.addEdge("b", "d", 3)
        graph.addEdge("d", "d", 4)
        graph.addEdge("c", "d", 5)
        graph.addEdge("d", "a", 6)
        graph.addEdge("c", "e", 7)
        graph.addEdge("d", "e", 8)
        graph.addEdge("e", "f", 9)
        graph.addEdge("f", "e", 10)
        graph.addEdge("e", "g", 11)
        graph.addEdge("g", "g", 12)
        graph.addEdge("g", "h", 13)
        graph.addEdge("e", "h", 14)
        graph.addEdge("i", "f", 15)
        graph.addEdge("j", "f", 16)
        graph.addEdge("i", "k", 17)
        graph.addEdge("k", "i", 18)
        graph.addEdge("i", "j", 19)
        graph.addEdge("j", "h", 20)
        graph.addEdge("j", "l", 21)
        graph.addEdge("l", "j", 22)
        graph.addEdge("l", "l", 23)
        graph.addEdge("l", "i", 24)
        graph.addEdge("j", "k", 25)
        graph.addEdge("k", "l", 26)
        graph.addEdge("k", "g", 27)
        graph.addEdge("k", "n", 28)
        graph.addEdge("n", "m", 29)
        graph.addEdge("m", "k", 30)

        return graph
    }

    private fun setupGraph4(graph: Graph<String, Int>): Graph<String, Int> {
        graph.addVertex("a")
        return graph
    }

    private fun setupGraph5(graph: Graph<String, Int>): Graph<String, Int> {
        graph.addVertex("a")
        graph.addEdge("a", "a", 1)
        return graph
    }

    private fun setupGraph6(graph: Graph<String, Int>): Graph<String, Int> {
        graph.addVertex("a")
        graph.addVertex("b")
        return graph
    }

    private fun setupGraph7(graph: Graph<String, Int>): Graph<String, Int> {
        graph.addVertex("a")
        graph.addVertex("b")
        graph.addEdge("a", "b", 1)
        return graph
    }

    private fun setupGraph8(graph: Graph<String, Int>): Graph<String, Int> {
        graph.addVertex("a")
        graph.addVertex("b")
        graph.addEdge("a", "b", 1)
        graph.addEdge("b", "a", 2)
        return graph
    }

    private fun setupGraph9(graph: Graph<String, Int>): Graph<String, Int> {
        graph.addVertex("a")
        graph.addVertex("b")
        graph.addVertex("c")
        graph.addEdge("a", "b", 1)
        graph.addEdge("b", "c", 2)
        graph.addEdge("c", "a", 3)
        return graph
    }

    private fun setupGraph10(graph: Graph<String, Int>): Graph<String, Int> {
        graph.addVertex("a")
        graph.addVertex("b")
        graph.addVertex("c")
        graph.addEdge("a", "b", 1)
        graph.addEdge("a", "c", 2)
        graph.addEdge("a", "a", 3)
        graph.addEdge("b", "a", 4)
        graph.addEdge("b", "c", 5)
        graph.addEdge("b", "b", 6)
        graph.addEdge("c", "b", 7)
        graph.addEdge("c", "c", 8)
        graph.addEdge("c", "a", 9)
        return graph
    }


    companion object {
        @JvmStatic
        private fun provideGraphsForTesting(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(DirectedWeightedGraph<String, Int>(), setOf<Set<String>>(), "Empty graph"),
                Arguments.of(GraphStronglyConnectedComponentTest().setupGraph1(DirectedWeightedGraph()), setOf(setOf("a", "b", "e"), setOf("c", "d", "h"), setOf("f", "g")), "Initial test graph"),
                Arguments.of(GraphStronglyConnectedComponentTest().setupGraph2(DirectedWeightedGraph()), setOf(setOf("a", "b", "e"), setOf("c", "d", "h"), setOf("f", "g"), setOf("k")), "Lonely vertex in initial test graph"),
                Arguments.of(GraphStronglyConnectedComponentTest().setupGraph3(DirectedUnweightedGraph()), setOf(setOf("a", "b", "d"), setOf("c"), setOf("e", "f"), setOf("g"), setOf("h"), setOf("i", "j", "k", "l", "m", "n")), "Complicated graph"),
                Arguments.of(GraphStronglyConnectedComponentTest().setupGraph4(DirectedUnweightedGraph()), setOf(setOf("a")), "One vertex"),
                Arguments.of(GraphStronglyConnectedComponentTest().setupGraph5(DirectedUnweightedGraph()), setOf(setOf("a")), "Loop of one vertex"),
                Arguments.of(GraphStronglyConnectedComponentTest().setupGraph6(DirectedUnweightedGraph()), setOf(setOf("a"), setOf("b")), "Two separate vertices"),
                Arguments.of(GraphStronglyConnectedComponentTest().setupGraph7(DirectedUnweightedGraph()), setOf(setOf("a"), setOf("b")), "Two connected vertices, one side"),
                Arguments.of(GraphStronglyConnectedComponentTest().setupGraph8(DirectedUnweightedGraph()), setOf(setOf("a", "b")), "Two connected vertices, two sides"),
                Arguments.of(GraphStronglyConnectedComponentTest().setupGraph9(DirectedUnweightedGraph()), setOf(setOf("a", "b", "c")), "A loop of three vertices"),
                Arguments.of(GraphStronglyConnectedComponentTest().setupGraph10(DirectedUnweightedGraph()), setOf(setOf("a", "b", "c")), "A very connected graph of three vertices")
            )
        }
    }

    @ParameterizedTest
    @MethodSource("provideGraphsForTesting")
    fun `test strongly connected components`(graph: Graph<String, Int>, expectedAnswer: Set<Set<String>>, @Suppress("UNUSED_PARAMETER") testName: String) {
        assertEquals(expectedAnswer, SCCsearch.getSCCs(graph))
    }
}
