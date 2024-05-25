package viewModel.workspace.graph

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import model.VertexModel

class VertexViewModel<V>(vertexModel: VertexModel<V>) : ViewModel() {
    private val _vertexState = MutableStateFlow(vertexModel)
    val vertexState: StateFlow<VertexModel<V>> = _vertexState

    fun setVertexOffset(offset: Offset) {
        _vertexState.update { currentState ->
            currentState.copy(
                x = offset.x,
                y = offset.y
            )
        }
    }

    fun incVertexOffset(incOffset: Offset) {
        _vertexState.update { currentState ->
            currentState.copy(
                x = currentState.x + incOffset.x,
                y = currentState.y + incOffset.y
            )
        }
    }

    fun setVertexSelect(isSelected: Boolean) {
        _vertexState.update { currentState ->
            currentState.copy(isSelected = isSelected)
        }
    }

    fun setVertexData(data: V?) {
        _vertexState.update { currentState ->
            currentState.copy(data = data)
        }
    }
}