package procon.xtom.tools

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Edit the XTom config file",
    ) {
        App()
    }
}