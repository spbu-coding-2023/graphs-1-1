package view.workspace

import androidx.compose.animation.*
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.FadeTransition
import view.home.HomeScreen
import view.settings.SettingsScreen

class WorkspaceScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopAppBar(navigator: Navigator, titleText: String) {
        var showMenu by remember { mutableStateOf(false) }
        TopAppBar(
            title = {
                Text(titleText)
            },
            navigationIcon = {
                IconButton(onClick = {
                    navigator.push(HomeScreen())
                }) {
                    Icon( Icons.Default.Home, "homeIcon")
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        showMenu = !showMenu
                    }
                ) {
                    Icon(Icons.Filled.MoreVert, "moreVertIcon")
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = !showMenu }) {
                        DropdownMenuItem(
                            text = {
                                Text("Settings")
                            }
                            ,onClick = {
                                navigator.push(SettingsScreen())
                                showMenu = !showMenu
                            }
                        )
                    }
                }
            }
        )
    }

    @Composable
    fun GraphView() {

    }

    @Composable
    fun GraphMouseTools(modifier: Modifier) {
        val listToolsDemo = listOf(Icons.Filled.Add, Icons.Filled.Menu, Icons.Filled.ThumbUp, Icons.Filled.Create)
        Column(
            modifier = Modifier
                .then(modifier),
                verticalArrangement = Arrangement.Center
        ) {
            listToolsDemo.forEach {
                SmallFloatingActionButton(
                    onClick = {},
                    modifier = Modifier
                        .padding(8.dp),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(it, "WW")
                }
            }
        }
    }

    @Composable
    fun GraphControlTools(modifier: Modifier) {
        var isShown by rememberSaveable { mutableStateOf(true) }
        Column(
            modifier = Modifier
                .then(modifier)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { isShown = !isShown },
                    modifier = Modifier
                ) {
                    Icon(Icons.Filled.Call, "settingsIcon")
                }
            }

            AnimatedVisibility(
                visible = isShown,
                enter = slideInHorizontally(initialOffsetX = { it }),
                exit = slideOutHorizontally(targetOffsetX = { it })
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 0.dp, 0.dp, 24.dp),
                    shadowElevation = 8.dp,
                    shape = MaterialTheme.shapes.medium.copy(
                        topEnd = CornerSize(0),
                        bottomEnd = CornerSize(0)
                    )
                ) {
                    Column(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    ) {
                        Text("Content")
                    }
                }
            }

            if (isShown) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    shadowElevation = 3.dp,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(

                    ) {
                        Text("Content")
                    }
                }
            }
        }
    }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = { TopAppBar(navigator, "(graph name)") },
            content = {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                ) {
//                    GraphView()
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GraphMouseTools(modifier = Modifier.fillMaxHeight())
                        Spacer(modifier = Modifier.weight(1f))
                        GraphControlTools(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(340.dp)
                        )
                    }
                }
            })
    }

}