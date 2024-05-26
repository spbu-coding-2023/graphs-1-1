package view.workspace.header.menu

import androidx.compose.animation.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import view.settings.SettingsScreen
import view.workspace.header.menu.shortcuts.ShortcutsDialog

@Composable
fun Menu(expanded: Boolean, onDismissRequest: () -> Unit) {
    var isShortcutsDialogShown by remember { mutableStateOf(false) }
    val navigator = LocalNavigator.currentOrThrow

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        // Settings item
        DropdownMenuItem(
            text = {
                Text("Settings")
            }
            ,onClick = {
                navigator.push(SettingsScreen())
                onDismissRequest()
            }
        )

        // Shortcuts item
        DropdownMenuItem(
            text = {
                Text("Shortcuts")
            }
            ,onClick = {
                isShortcutsDialogShown = !isShortcutsDialogShown
                onDismissRequest()
            }
        )
    }

    // shortcuts dialog
    if (isShortcutsDialogShown) {
        ShortcutsDialog(
            onDismissRequest = {
                isShortcutsDialogShown = !isShortcutsDialogShown
            }
        )
    }
}