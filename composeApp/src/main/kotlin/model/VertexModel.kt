package model

data class VertexModel<V>(val id: Int, var x: Float, var y: Float, var data: V?, var isSelected: Boolean = false) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VertexModel<*>) return false
        return id == (other as? VertexModel<*>)?.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
