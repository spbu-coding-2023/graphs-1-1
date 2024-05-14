package graph.utils

class AdjacencyMatrix<V, E> {
    val matrix = mutableListOf<MutableList<E?>>()
    val verteciesMap = HashMap<V, Int>()
    fun get(tail: V, head: V): E? {
        val tailIndex = verteciesMap[tail]
        val headIndex = verteciesMap[head]
        if (tailIndex == null || headIndex == null) throw IllegalArgumentException("Can not get edge by vertecies: Both vertecies must exist")
        return matrix[tailIndex][headIndex]
    }

    fun set(tail: V, head: V, e: E?): Boolean {
        val tailIndex = verteciesMap[tail]
        val headIndex = verteciesMap[head]
        if (tailIndex == null || headIndex == null) throw IllegalArgumentException("Can not set edge by vertecies: Both vertecies must exist")
        val prev = matrix[tailIndex][headIndex]
        matrix[tailIndex][headIndex] = e
        if (prev == null) return true
        return false
    }

    fun add(v: V): Boolean {
        if (v in verteciesMap) return false

        val matrixSize = matrix.size
        verteciesMap[v] = matrixSize

        matrix.forEach {// adds by one line (could double each time, would be faster ig)
            it.add(null)
        }
        matrix.add(MutableList(matrixSize+1) {null})
        return true
    }

    fun delete(v: V) {
        val vertexIndex = verteciesMap[v] ?: throw IllegalArgumentException("Can not delete vertex: vertex does not exist")

        matrix.forEach {
            it.removeAt(vertexIndex)
        }

        matrix.removeAt(vertexIndex)

        verteciesMap.remove(v)

        verteciesMap.forEach {
            if (it.value > vertexIndex) {
                verteciesMap[it.key] = it.value - 1
            }
        }
    }
}