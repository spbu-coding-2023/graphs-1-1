package integration

import display.minimumSpanningTree.implementation.GraphMSTWithKruskal
import graph.implementation.UndirectedUnweightedGraph
import model.EdgeModel
import model.VertexModel
import viewModel.workspace.graph.GraphViewModel
import org.junit.jupiter.api.Test

class ShowMstTest {
    @Test
    fun showMst() {
        // create graph
        val graphViewModel = GraphViewModel(UndirectedUnweightedGraph(), "testGraph")

        // setup graph
        graphViewModel.updateGraph { g ->
            val vertex1 = VertexModel(0, 0f,0f, null)
            val vertex2 = VertexModel(1, 0f,0f, null)
            val vertex3 = VertexModel(2, 0f,0f, null)
            val vertex4 = VertexModel(3, 0f,0f, null)

            g.addVertex(vertex1)
            g.addVertex(vertex2)
            g.addVertex(vertex3)
            g.addVertex(vertex4)

            g.addEdge(vertex1, vertex2, EdgeModel(0, 1, null, isDirected = false, isWeighted = false))
            g.addEdge(vertex1, vertex2, EdgeModel(1, 2, null, isDirected = false, isWeighted = false))
            g.addEdge(vertex1, vertex2, EdgeModel(2, 3, null, isDirected = false, isWeighted = false))
            g.addEdge(vertex1, vertex2, EdgeModel(3, 1, null, isDirected = false, isWeighted = false))
        }

        // run the mst algorithm and check
        var highlightedEdges = 0
        graphViewModel.runMST(GraphMSTWithKruskal()) {
            graphViewModel.edges.value.forEach { edge ->
                highlightedEdges += if (edge.isHighlighted) 1 else 0
            }

            assert(highlightedEdges == 3)
        }

        // clean the graph
        graphViewModel.storage.deleteGraph()
    }

}