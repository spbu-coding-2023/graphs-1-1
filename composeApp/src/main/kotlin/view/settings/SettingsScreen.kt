package view.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import view.common.CommonScreenLayout

class SettingsScreen : Screen {

    @Composable
    override fun Content() {
        CommonScreenLayout(
            title = "Settings",
            content = {
                Text("Settings are a bit empty")

            }
        )
    }

}