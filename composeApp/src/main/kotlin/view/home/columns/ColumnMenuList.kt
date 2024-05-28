package view.home.columns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import view.create.CreateScreen
import view.home.header.HeaderLogo
import view.settings.SettingsScreen
import viewModel.workspace.graph.GraphsContainerViewModel

@Composable
fun ColumnMenuList(modifier: Modifier, graphsContainerViewModel: GraphsContainerViewModel) {
    val navigator = LocalNavigator.currentOrThrow

    Column(
        modifier = Modifier.fillMaxHeight().then(modifier)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),

        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp, 32.dp)
                    .fillMaxWidth(),
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

            Spacer(modifier = Modifier.weight(1f))

            HeaderLogo()
        }
    }
}
