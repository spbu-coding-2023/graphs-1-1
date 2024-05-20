package view.workspace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import view.workspace.graphConfiguration.GraphConfiguration
import view.workspace.graphToolBar.GraphToolBar
import view.workspace.graph.GraphView
import view.workspace.header.Header

class WorkspaceScreen : Screen {
    @Composable
    override fun Content() {
        Scaffold(
            topBar = {
                // the header of the screen
                Header("(graph name)")
            },
            content = {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                ) {
                    // draw graph
                    GraphView(
                        modifier = Modifier.fillMaxSize()
                    )

                    // list workspace elements in a row
                    Row(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        // the tools to change behavior of the mouse
                        GraphToolBar(
                            modifier = Modifier.fillMaxHeight()
                        )

                        // empty space
                        Spacer(
                            modifier = Modifier.weight(1f)
                        )

                        // the sidebar to configure current graph
                        GraphConfiguration(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(340.dp)
                        )
                    }
                }
            }
        )
    }

}