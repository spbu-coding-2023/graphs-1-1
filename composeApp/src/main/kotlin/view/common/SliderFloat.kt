package view.common

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderFloat(text: String, floatRangeFrom: Float, floatRangeTo: Float, steps: Int, onChange: (Float) -> Unit) {
    var sliderPosition by remember { mutableStateOf(0f) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,

    ) {
        Text(
            text = "$text: ${round(sliderPosition*100)/100}",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize
        )

        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                onChange(round(sliderPosition*100) /100)
            },
            valueRange = floatRangeFrom..floatRangeTo,
            steps = steps,
        )
    }
}
