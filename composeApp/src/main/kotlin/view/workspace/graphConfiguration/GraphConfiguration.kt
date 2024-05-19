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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GraphConfiguration(modifier: Modifier) {
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