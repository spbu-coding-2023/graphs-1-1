package graphs

import graph.implementation.UndirectedUnweightedGraph
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class AdjacencyMatrixTest {
    var graph = UndirectedUnweightedGraph<Int, String>()
    var structure = graph.AdjacencyMatrix<Int, String>()

    @BeforeEach
    fun setup() {
        structure = graph.AdjacencyMatrix()
    }

    @Test
    fun `get matrix`() {
        structure.add(23)

        assertEquals(structure.matrix, mutableListOf(mutableListOf(null)))
    }

    @Test
    fun `get vertices map`() {
        val res = structure.add(23)
        val hm = HashMap<Int, Int>()
        hm[23] = 0
        assertEquals(structure.verticesMap, hm)
        assertTrue(res)
    }

    @Test
    fun `get`() {
        structure.add(23)
        structure.add(52)
        structure.add(1)

        structure.set(23, 52, "A")

        assertEquals(structure.get(23, 52), "A")
    }

    @Test
    fun `set`() {
        structure.add(23)
        structure.add(52)
        structure.add(1)

        val res = structure.set(23, 52, "A")

        assertEquals(structure.matrix[0][1], "A")
        assertTrue(res)
    }

    @Test
    fun `add`() {
        structure.add(23)
        structure.add(52)
        structure.add(1)
        val hm = HashMap<Int, Int>()
        hm[23] = 0
        hm[52] = 1
        hm[1] = 2
        assertEquals(structure.verticesMap, hm)
    }

    @Test
    fun delete() {
        structure.add(23)
        structure.add(52)
        structure.add(1)

        structure.set(23, 52, "A")

        structure.delete(52)
        val hm = HashMap<Int, Int>()
        hm[23] = 0
        hm[1] = 1
        assertEquals(structure.verticesMap, hm)
    }

    @Test
    fun `should throw IllegalArgumentException since tail doesn't exist function get`() {
        structure.add(1)
        assertThrows(IllegalArgumentException::class.java) {
            structure.get(0, 1)
        }
    }

    @Test
    fun `should throw IllegalArgumentException since tail doesn't exist function set`() {
        structure.add(1)
        assertThrows(IllegalArgumentException::class.java) {
            structure.set(0, 1, "A")
        }
    }

    @Test
    fun `should throw IllegalArgumentException since head doesn't exist function get`() {
        structure.add(0)
        assertThrows(IllegalArgumentException::class.java) {
            structure.get(0, 1)
        }
    }

    @Test
    fun `should throw IllegalArgumentException since head doesn't exist function set`() {
        structure.add(0)
        assertThrows(IllegalArgumentException::class.java) {
            structure.set(0, 1, "A")
        }
    }

    @Test
    fun `should throw IllegalArgumentException since vertex to be deleted doesn't exist`() {
        assertThrows(IllegalArgumentException::class.java) {
            structure.delete(0)
        }
    }
}
