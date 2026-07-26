package co.akoot

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import co.akoot.akui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "AKUI",
    ) {
        App()
    }
}