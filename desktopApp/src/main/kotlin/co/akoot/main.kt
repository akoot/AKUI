package co.akoot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import co.akoot.akui.App
import co.akoot.akui.Theme

//val theme = Theme(background = Color.Black, primary = Color.White)
val theme = Theme()

var title by mutableStateOf("AKUI")

fun main() = application {
    val state = rememberWindowState(position = WindowPosition.Aligned(Alignment.Center))
    Window(
        onCloseRequest = ::exitApplication,
        title = title,
        undecorated = true,
        transparent = true,
        state = state
    ) {
        Column(
            Modifier
                .clip(RoundedCornerShape(if (state.placement == WindowPlacement.Floating) 24.dp else 0.dp))
                .background(theme.background)
        ) {
            WindowDraggableArea {
                theme.TopBar(state, title, onClose = ::exitApplication)
            }
            App(theme)
        }
    }
}