package model

data class EdgeModel<E>(val tailVertexId: Int, val headVertexId: Int, val data: E?)
