package co.akoot.akui

import java.io.File

val folder = File(".akui")
actual fun getThemes(): List<Theme> {
    val themeFolder = File("")
    return emptyList()
}

actual fun getSettings(): Settings {
    return Settings()
}