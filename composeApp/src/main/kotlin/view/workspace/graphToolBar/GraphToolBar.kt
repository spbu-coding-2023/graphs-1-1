package view.workspace.graphToolBar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GraphToolBar(modifier: Modifier) {
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