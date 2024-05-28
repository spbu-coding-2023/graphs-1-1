package view.home.columns

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import view.home.columns.deleteDialog.DeleteGraphDialog
import view.workspace.WorkspaceScreen
import viewModel.workspace.graph.GraphViewModel
import viewModel.workspace.graph.GraphsContainerViewModel
import viewModel.workspace.graph.utils.LocalDatabase

@Composable
fun GraphRepresentation(viewModel: GraphViewModel, graphsContainerViewModel: GraphsContainerViewModel, iconBgColor: Color, titleColor: Color, subTitleColor: Color) {
    var isDeleteDialogShown by remember { mutableStateOf(false) }
    val navigator = LocalNavigator.currentOrThrow

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Surface(
           modifier = Modifier
               .size(48.dp),
           shape = MaterialTheme.shapes.medium,
            color = iconBgColor
        ) {
            Text(
                text = viewModel.graphName.slice(0..1).uppercase(),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = Color.White
            )
        }

        Column(
            modifier = Modifier
                .clickable {
                    navigator.push(WorkspaceScreen(viewModel))
                },
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Bottom)
        ) {
            Text(
                text = viewModel.graphName,
                color = titleColor,
                fontSize = MaterialTheme.typography.labelMedium.fontSize
            )

            Text(
                text = "${LocalDatabase.getGraphsDirectory()}/${viewModel.graphName}",
                color = subTitleColor,
                fontSize = MaterialTheme.typography.labelSmall.fontSize
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            modifier = Modifier,
            onClick = {
                isDeleteDialogShown = !isDeleteDialogShown
            }
        ) {
            Icon(Icons.Filled.DeleteForever, "deleteIcon")
        }

    }

    if (isDeleteDialogShown) {
        DeleteGraphDialog(
            onDismissRequest = {
                isDeleteDialogShown = !isDeleteDialogShown
            },
            onConfirmRequest = {
                graphsContainerViewModel.deleteGraph(viewModel.graphName)
                isDeleteDialogShown = !isDeleteDialogShown
            }
        )
    }
}
