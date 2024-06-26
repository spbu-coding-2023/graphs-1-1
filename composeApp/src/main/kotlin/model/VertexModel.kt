package model

data class VertexModel(val id: Int, var x: Float, var y: Float, var data: String?, var isSelected: Boolean = false, var size: Float = 1f, var communityId: Int = 0) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VertexModel) return false
        return id == (other as? VertexModel)?.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
