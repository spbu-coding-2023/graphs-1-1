package viewModel.workspace

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SelectAreaState(val startOffset: Offset, val areaSize: Offset) // TODO: Move to model

class SelectAreaViewModel : ViewModel() {
    private val _selectAreaState = MutableStateFlow(SelectAreaState(Offset.Zero, Offset.Zero))
    val selectAreaState: StateFlow<SelectAreaState> = _selectAreaState

    fun setStartOffset(offset: Offset) {
        _selectAreaState.value.copy(
            startOffset = offset
        )
    }

    fun setAreaSize(size: Offset) {
        _selectAreaState.value.copy(
            areaSize = size
        )
    }

    fun incAreaSize(incSize: Offset) {
        _selectAreaState.value.copy(
            areaSize = _selectAreaState.value.areaSize + incSize
        )
    }
}