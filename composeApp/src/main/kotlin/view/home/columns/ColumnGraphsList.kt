package view.home.columns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text2.BasicSecureTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import viewModel.workspace.graph.GraphsContainerViewModel

@Composable
fun ColumnGraphsList(modifier: Modifier, graphsContainerViewModel: GraphsContainerViewModel) {
    val graphsContainer by graphsContainerViewModel.graphsContainer.collectAsState()
    var searchText by remember { mutableStateOf("") }

    Column {
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.background).padding(8.dp, 16.dp)
        ) {
           OutlinedTextField(
               value = searchText,
               onValueChange = {
                   searchText = it
               },
               label = { Text("Search") },
               modifier = Modifier.padding(4.dp).fillMaxWidth()
           )
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .clip(shape = MaterialTheme.shapes.large.copy(
                    topEnd = CornerSize(0),
                    bottomEnd = CornerSize(0),
                    bottomStart = CornerSize(0),
                ))
                .verticalScroll(rememberScrollState())
                .then(modifier),
            tonalElevation = 2.dp,
        ) {
            Column(
                modifier = Modifier.padding(0.dp, 8.dp).fillMaxHeight()
            ) {
                val listedGraphs = graphsContainer.filter { g -> g.graphName.lowercase().startsWith(searchText.lowercase()) }
                if (listedGraphs.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "no graphs are found",
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                        )
                    }
                } else {
                    listedGraphs.forEach { graph ->
                        GraphRepresentation(
                            viewModel = graph,
                            graphsContainerViewModel = graphsContainerViewModel,
                            titleColor = MaterialTheme.colorScheme.primary,
                            subTitleColor = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}
