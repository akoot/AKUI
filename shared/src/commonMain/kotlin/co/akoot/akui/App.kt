package co.akoot.akui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//val theme = Theme(background = Color.Black, primary = Color.White)
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
            Spacer(Modifier.padding(12.dp))
            theme.PasswordField(icon = { Icon(Icons.Default.Lock, "Password") }) {
                println("password: $it")
            }
            Spacer(Modifier.padding(12.dp))
            theme.TextField(placeholder = "Placeholder...") {
                println("text: $it")
                true
            }
            Spacer(Modifier.padding(12.dp))
            theme.TextField() {
                println("text: $it")
                true
            }
            Spacer(Modifier.padding(12.dp))
            theme.TextField(Context.SECONDARY, icon = { Icon(Icons.Default.Settings, "Erm") }) {
                println("text: $it")
                false
            }
            Spacer(Modifier.padding(12.dp))
        }
    }
}