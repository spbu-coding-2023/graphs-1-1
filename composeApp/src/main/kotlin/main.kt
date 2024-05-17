import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import view.App
import java.awt.Dimension

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Graphses",
    ) {
        window.minimumSize = Dimension(840, 480)
        App()
    }
}
