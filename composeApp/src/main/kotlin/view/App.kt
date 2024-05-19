package view

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import view.home.HomeScreen

@Composable
fun App() {
    MaterialTheme {
        Navigator(HomeScreen()) { navigator ->
            FadeTransition(navigator, animationSpec = TweenSpec(durationMillis = 100, easing = LinearEasing), modifier = Modifier.fillMaxSize())
        }
    }
}
