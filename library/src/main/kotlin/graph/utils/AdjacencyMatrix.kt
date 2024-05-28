package graph.utils

class AdjacencyMatrix<V, E> {
    val matrix = mutableListOf<MutableList<E?>>()
    val verticesMap = HashMap<V, Int>()
    fun get(tail: V, head: V): E? {
        val tailIndex = verticesMap[tail]
        val headIndex = verticesMap[head]
        if (tailIndex == null || headIndex == null)
            throw IllegalArgumentException("Can not get edge by vertices: Both vertices must exist")
        return matrix[tailIndex][headIndex]
    }

    fun set(tail: V, head: V, e: E?): Boolean {
        val tailIndex = verticesMap[tail]
        val headIndex = verticesMap[head]
        if (tailIndex == null || headIndex == null)
            throw IllegalArgumentException("Can not set edge by vertices: Both vertices must exist")
        val prev = matrix[tailIndex][headIndex] == null
        matrix[tailIndex][headIndex] = e
        return prev
    }

    fun add(v: V): Boolean {
        if (v in verticesMap) return false

        val matrixSize = matrix.size
        verticesMap[v] = matrixSize

        matrix.forEach {// adds by one line (could double each time, would be faster ig)
            it.add(null)
        }
        matrix.add(MutableList(matrixSize+1) {null})
        return true
    }

    fun delete(v: V) {
        val vertexIndex = verticesMap[v] ?:
            throw IllegalArgumentException("Can not delete vertex: vertex does not exist")

        matrix.forEach {
            it.removeAt(vertexIndex)
        }

        matrix.removeAt(vertexIndex)

        verticesMap.remove(v)

        verticesMap.forEach {
            if (it.value > vertexIndex) {
                verticesMap[it.key] = it.value - 1
            }
        }
    }
}
