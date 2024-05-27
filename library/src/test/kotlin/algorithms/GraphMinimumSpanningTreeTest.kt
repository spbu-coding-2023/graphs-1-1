package algorithms

import display.minimumSpanningTree.implementation.GraphMSTWithKruskal
import graph.Graph
import graph.implementation.DirectedWeightedGraph
import graph.implementation.DirectedUnweightedGraph
import graph.implementation.UndirectedUnweightedGraph
import graph.implementation.UndirectedWeightedGraph
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class GraphMinimumSpanningTreeTest {
    val MSTsearch = GraphMSTWithKruskal()
    private fun setupGraph1(graph: Graph<String, String>): Graph<String, String> {
        graph.addVertex("a")
        graph.addVertex("b")
        graph.addVertex("c")
        graph.addVertex("d")
        graph.addVertex("e")
        graph.addVertex("f")
        graph.addVertex("g")
        graph.addVertex("h")

        graph.addEdge("a", "b", "ab", 7.0)
        graph.addEdge("b", "e", "be", 2.0)
        graph.addEdge("e", "a", "ea", 6.0)
        graph.addEdge("b", "f", "bf", 5.0)
        graph.addEdge("e", "f", "ef", 4.0)
        graph.addEdge("f", "g", "fg", 9.0)
        graph.addEdge("b", "c", "bc", 3.0)
        graph.addEdge("c", "g", "cg", 1.0)
        graph.addEdge("c", "d", "cd", 10.0)
        graph.addEdge("d", "h", "dh", 4.0)
        graph.addEdge("g", "h", "gh", 11.0)

        return graph
    }

    private fun setupGraph2(graph: Graph<String, String>): Graph<String, String> {
        val newGraph = setupGraph1(graph)
        newGraph.addVertex("k")
        return newGraph
    }

    private fun setupGraph3(graph: Graph<String, String>): Graph<String, String> {
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

        graph.addEdge("a", "b", "ab", 20.0)
        graph.addEdge("c", "b", "cb", 15.0)
        graph.addEdge("b", "d", "bd", 22.0)
        graph.addEdge("c", "d", "cd", 19.0)
        graph.addEdge("d", "a", "da", 2.0)
        graph.addEdge("c", "e", "ce", 23.0)
        graph.addEdge("d", "e", "de", 10.0)
        graph.addEdge("e", "f", "ef", 4.0)
        graph.addEdge("e", "g", "eg", 17.0)
        graph.addEdge("g", "h", "gh", 8.0)
        graph.addEdge("e", "h", "eh", 14.0)
        graph.addEdge("i", "f", "if", 11.0)
        graph.addEdge("j", "f", "jf", 12.0)
        graph.addEdge("i", "k", "ik", 6.0)
        graph.addEdge("i", "j", "ij", 18.0)
        graph.addEdge("j", "h", "jh", 7.0)
        graph.addEdge("j", "l", "jl", 16.0)
        graph.addEdge("l", "i", "li", 24.0)
        graph.addEdge("j", "k", "jk", 13.0)
        graph.addEdge("k", "l", "kl", 21.0)
        graph.addEdge("k", "g", "kg", 3.0)
        graph.addEdge("k", "n", "kn", 5.0)
        graph.addEdge("n", "m", "nm", 12.0)
        graph.addEdge("m", "k", "mk", 9.0)

        return graph
    }

    private fun setupGraph4(graph: Graph<String, String>): Graph<String, String> {
        graph.addVertex("a")
        return graph
    }

    private fun setupGraph5(graph: Graph<String, String>): Graph<String, String> {
        graph.addVertex("a")
        graph.addEdge("a", "a", "aa", 5.0)
        return graph
    }

    private fun setupGraph6(graph: Graph<String, String>): Graph<String, String> {
        graph.addVertex("a")
        graph.addVertex("b")
        return graph
    }

    private fun setupGraph7(graph: Graph<String, String>): Graph<String, String> {
        graph.addVertex("a")
        graph.addVertex("b")
        graph.addEdge("a", "b", "ab")
        return graph
    }

    private fun setupGraph8(graph: Graph<String, String>): Graph<String, String> {
        graph.addVertex("a")
        graph.addVertex("b")
        graph.addEdge("a", "b", "ab")
        graph.addEdge("b", "a", "ba", 8.0)
        return graph
    }

    private fun setupGraph9(graph: Graph<String, String>): Graph<String, String> {
        graph.addVertex("a")
        graph.addVertex("b")
        graph.addVertex("c")
        graph.addEdge("a", "b", "ab", 3.0)
        graph.addEdge("b", "c", "bc", 2.0)
        graph.addEdge("c", "a", "ca", 5.0)
        return graph
    }

    companion object {
        @JvmStatic
        private fun provideGraphsForTesting(): Stream<Arguments> {
            return Stream.of(
                    Arguments.of(DirectedUnweightedGraph<String, String>(), null, "Should not work for directed graphs"),
                    Arguments.of(DirectedWeightedGraph<String, String>(), null, "Should not work for directed graphs"),
                    Arguments.of(UndirectedWeightedGraph<String, String>(), setOf<Set<String>>(), "Empty graph: unweighted"),
                    Arguments.of(GraphMinimumSpanningTreeTest().setupGraph1(UndirectedUnweightedGraph()), setOf("ab", "ea", "ef", "fg", "gh", "cg", "cd"), "Initial test graph: unweighted"),
                    Arguments.of(GraphMinimumSpanningTreeTest().setupGraph2(UndirectedUnweightedGraph()), setOf("ab", "ea", "ef", "fg", "gh", "cg", "cd"), "Lonely vertex in initial test graph: unweighted"),
                    Arguments.of(GraphMinimumSpanningTreeTest().setupGraph3(UndirectedUnweightedGraph()), setOf("ab", "da", "de", "cd", "ef", "eg", "eh", "jh", "jk", "jl", "ij", "kn", "mk"), "Complicated graph: unweighted"),
                    Arguments.of(GraphMinimumSpanningTreeTest().setupGraph4(UndirectedUnweightedGraph()), setOf<String>(), "One vertex: unweighted"),
                    Arguments.of(GraphMinimumSpanningTreeTest().setupGraph5(UndirectedUnweightedGraph()), setOf<String>(), "Loop of one vertex: unweighted"),
                    Arguments.of(GraphMinimumSpanningTreeTest().setupGraph6(UndirectedUnweightedGraph()), setOf<String>(), "Two separate vertices: unweighted"),
                    Arguments.of(GraphMinimumSpanningTreeTest().setupGraph7(UndirectedUnweightedGraph()), setOf("ab"), "Two connected vertices: unweighted"),
                    Arguments.of(GraphMinimumSpanningTreeTest().setupGraph9(UndirectedUnweightedGraph()), setOf("ab", "ca"), "A loop of three vertices: unweighted"),

                    Arguments.of(UndirectedWeightedGraph<String, String>(), setOf<Set<String>>(), "Empty graph: weighted"),
                    Arguments.of(GraphMinimumSpanningTreeTest().setupGraph1(UndirectedWeightedGraph()), setOf("cg", "be", "bc", "dh", "ef", "ea", "cd"), "Initial test graph: weighted"),
                    Arguments.of(GraphMinimumSpanningTreeTest().setupGraph2(UndirectedWeightedGraph()), setOf("cg", "be", "bc", "dh", "ef", "ea", "cd"), "Lonely vertex in initial test graph: weighted"),
                    Arguments.of(GraphMinimumSpanningTreeTest().setupGraph3(UndirectedWeightedGraph()), setOf("da", "kg", "ef", "kn", "ik", "jh", "gh", "mk", "de", "if", "cb", "jl", "cd"), "Complicated graph: weighted"),
                    Arguments.of(GraphMinimumSpanningTreeTest().setupGraph4(UndirectedWeightedGraph()), setOf<String>(), "One vertex: weighted"),
                    Arguments.of(GraphMinimumSpanningTreeTest().setupGraph5(UndirectedWeightedGraph()), setOf<String>(), "Loop of one vertex: weighted"),
                    Arguments.of(GraphMinimumSpanningTreeTest().setupGraph6(UndirectedWeightedGraph()), setOf<String>(), "Two separate vertices: weighted"),
                    Arguments.of(GraphMinimumSpanningTreeTest().setupGraph7(UndirectedWeightedGraph()), setOf("ab"), "Two connected vertices: weighted"),
                    Arguments.of(GraphMinimumSpanningTreeTest().setupGraph9(UndirectedWeightedGraph()), setOf("bc", "ab"), "A loop of three vertices: weighted")
            )
        }
    }

    @ParameterizedTest
    @MethodSource("provideGraphsForTesting")
    fun `test strongly connected components`(graph: Graph<String, String>, expectedAnswer: Set<String>?, @Suppress("UNUSED_PARAMETER") testName: String) {
        assertEquals(expectedAnswer, MSTsearch.getMST(graph))
    }
}