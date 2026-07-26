package co.akoot.akui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val theme = Theme()

@Composable
@Preview
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .background(theme.background)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            theme.Button(onClick = { println("ERM") }) {
                Text("Click me!")
            }
            theme.Button(Context.SECONDARY, onClick = { println("ERM") }) {
                Text("Click me!")
            }
            theme.Button(Context.TERTIARY, onClick = { println("ERM") }) {
                Text("Click me!")
            }
            theme.Button(Context.WARNING, onClick = { println("ERM") }) {
                Text("Click me!")
                Icon(Icons.Default.Close, "Click me!")
            }
            theme.IconButton(Context.ERROR, onClick = { println("ERM") }) {
                Icon(Icons.Default.Close, "Click me!")
            }
            theme.Button(Context.SUCCESS, onClick = { println("ERM") }) {
                Text("Click me!")
            }
            theme.Button(Context.QUOTE, onClick = { println("ERM") }) {
                Text("Click me!")
            }
        }
    }
}