package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import view.home.HomeScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(screen = HomeScreen()) { navigator ->
            CurrentScreen()
        }
    }
}
