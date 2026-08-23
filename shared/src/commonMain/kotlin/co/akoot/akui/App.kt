package co.akoot.akui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



@Composable
fun App(theme: Theme) {
    theme.apply {
        Column(
            modifier = Modifier
                .background(theme.background)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(12.dp)
            PasswordField(icon = { Icon(Icons.Default.Lock) }) {
                println("password: $it")
            }
            Spacer(12.dp)
            TextField(placeholder = "Placeholder...") {
                println("text: $it")
                true
            }
            Spacer(12.dp)
            TextField() {
                println("text: $it")
                true
            }
            Spacer(Modifier.padding(12.dp))
            TextField(Context.SECONDARY, icon = { Icon(Icons.Default.Settings) }) {
                println("text: $it")
                false
            }
            Spacer(12.dp)
            Button("Click") {
                println("click")
            }
        }
    }
}