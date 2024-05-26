import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import view.App
import java.awt.Dimension

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
    ) {
        MenuBar {
            Menu("File") {
                Item("Open...", onClick = {  })
            }
        }
        window.minimumSize = Dimension(840, 480)
        App()
    }
}
