package viewModel.workspace.graph

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import model.EdgeModel

class EdgeViewModel<E>(edgeModel: EdgeModel<E>) : ViewModel() {
    private val _edgeState = MutableStateFlow(edgeModel)
    val edgeState: StateFlow<EdgeModel<E>> = _edgeState

    fun setEdgeData(data: E?) {
        _edgeState.value.copy(
            data = data
        )
    }
}