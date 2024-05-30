package view.workspace.graphConfiguration

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConfigurationOption(modifier: Modifier, title: String, description: String, onClick: () -> Unit, enabled: Boolean, content: (@Composable () -> Unit)? = null) {
    Surface(
        modifier = modifier,
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp, 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
        ) {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top)
            ) {
                Text(
                    text = title,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize
                )
                Text(
                    text = description,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .7f)
                )
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                tonalElevation = 8.dp,
                shape = MaterialTheme.shapes.medium
            ) {
                if (content != null) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)) {
                        content()
                    }
                }
            }

            Button(
                onClick = onClick,
                enabled = enabled
            ) {
                Text("Apply")
            }

        }
    }
}