package view.workspace

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import view.home.HomeScreen

class WorkspaceScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Column {
            Text("Here is your graphs")

            Button(
                onClick = {
                    navigator.push(HomeScreen())
                },
            ) {
                Text("Home")
            }
        }
    }

}