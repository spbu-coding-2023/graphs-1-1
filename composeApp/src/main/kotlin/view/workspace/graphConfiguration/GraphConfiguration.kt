package view.workspace.graphConfiguration

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import display.placement.implementation.GraphPlacementRandom
import display.placement.implementation.GraphPlacementYifanHu
import viewModel.workspace.graph.GraphViewModel

@Composable
fun GraphConfiguration(viewModel: GraphViewModel, modifier: Modifier) {
    var isShown by rememberSaveable { mutableStateOf(true) }
    var btnColorIsRunning by remember { mutableStateOf(false) }
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
                    // insides
                    // YH
                    Button(
                        onClick = {
                            btnColorIsRunning = true
                            val graphPlacement = GraphPlacementYifanHu()
                            graphPlacement.setSize(4, 4)
                            viewModel.runPlacement(graphPlacement, { btnColorIsRunning = false })
                        }
                    ) {
                        Text(
                        text = "run placement in parallel YH",
                        color = if (btnColorIsRunning) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground
                    ) }
                    // RAND
                    Button(
                        onClick = {
                            btnColorIsRunning = true
                            val graphPlacement = GraphPlacementRandom()
                            graphPlacement.setSize(600, 600)
                            viewModel.runPlacement(graphPlacement, { btnColorIsRunning = false })
                        }
                    ) {
                        Text(
                            text = "run placement in parallel RANDOM",
                            color = if (btnColorIsRunning) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground
                        ) }
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