package model

data class EdgeModel(val tailVertexId: Int, val headVertexId: Int, val data: String?, var isDirected: Boolean = true, var isWeighted: Boolean = false, var isHighlighted: Boolean = false, var weight: Double = 1.0)
