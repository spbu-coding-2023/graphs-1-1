package model

data class EdgeModel(val tailVertexId: Int, val headVertexId: Int, val data: String?, var isDirected: Boolean = true)
