package view

import androidx.compose.material3.*
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import view.home.HomeScreen

@Composable
fun App() {
    MaterialTheme {
        Navigator(HomeScreen()) { navigator ->
            CurrentScreen()
        }
    }
}
