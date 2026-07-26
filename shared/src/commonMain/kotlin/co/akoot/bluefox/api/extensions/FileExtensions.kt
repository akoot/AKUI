package co.akoot.bluefox.api.extensions

import co.akoot.bluefox.api.util.FileUtil
import java.io.File
import java.util.Base64

fun File.replaceWith(loader: ClassLoader, path: String): Boolean {
    return FileUtil.extractFile(loader, path, toPath(), true)
}

fun File.touch(content: Any? = null): File {
    ifNotExists {
        parentFile.mkdirs()
        createNewFile()
        content?.let {
            when(content) {
                is ByteArray -> writeBytes(it as ByteArray)
                is String -> writeText(it as String)
                else -> writeText(it.toString())
            }
        }
    }
    return this
}

fun File.mkdirp(): File {
    mkdirs()
    return this
}

fun File.ifExists(block: File.() -> Unit): File {
    if(exists()) block()
    return this
}

fun File.ifNotExists(block: File.() -> Unit): File {
    if(!exists()) block()
    return this
}

infix fun File.or(file: File): File {
    return if (exists()) this else file
}

val File.toBase64: String
    get() {
        require(exists()) { "File does not exist: $absolutePath" }

        val bytes = readBytes()
        return Base64.getEncoder().encodeToString(bytes)
    }