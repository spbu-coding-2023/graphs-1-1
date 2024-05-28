package view.home.columns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import view.create.CreateScreen
import view.settings.SettingsScreen
import viewModel.workspace.graph.GraphsContainerViewModel

@Composable
fun ColumnMenuList(modifier: Modifier, graphsContainerViewModel: GraphsContainerViewModel) {
    val navigator = LocalNavigator.currentOrThrow

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp, 32.dp)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        ButtonMenuItem(
            text = "New Graph",
            onClick = {
                navigator.push(CreateScreen(graphsContainerViewModel))
            }
        )

        ButtonMenuItem(
            text = "Settings",
            onClick = {
                navigator.push(SettingsScreen())
            }
        )
    }
}
