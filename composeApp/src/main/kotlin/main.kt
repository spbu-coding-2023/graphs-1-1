import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Dimension

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Graphses",
    ) {
        window.minimumSize = Dimension(450, 190)
        App()
    }
}
