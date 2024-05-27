package view.workspace.header

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import view.home.HomeScreen
import view.workspace.header.menu.Menu
import viewModel.workspace.graph.GraphViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(viewModel: GraphViewModel) {
    var isMenuOpen by remember { mutableStateOf(false) }
    val navigator = LocalNavigator.currentOrThrow

    TopAppBar(
        title = {
            Text(viewModel.graphName)
        },
        navigationIcon = {
            // Home button
            IconButton(
                onClick = {
                    viewModel.storage.exportGraph()
                    navigator.push(HomeScreen())
                }
            ) {
                Icon( Icons.Default.Home, "homeIcon")
            }
        },
        actions = {
            // Search
            IconButton(
                onClick = {  }
            ) {
                Icon(Icons.Filled.Search, "searchIcon")
            }
            // Menu
            IconButton(
                onClick = {
                    isMenuOpen = !isMenuOpen
                }
            ) {
                Icon(Icons.Filled.MoreVert, "moreVertIcon")

                // Drop down menu
                Menu(
                    expanded = isMenuOpen,
                    onDismissRequest = {
                        isMenuOpen = !isMenuOpen
                    }
                )
            }
        }
    )
}
