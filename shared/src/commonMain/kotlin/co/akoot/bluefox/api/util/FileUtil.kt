package co.akoot.bluefox.api.util

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.Base64
import kotlin.io.path.exists
import kotlin.text.replace

object FileUtil {

    private val cleanFileNameRegex = Regex("[<>:\"/\\\\|?*\\x00-\\x1F]")

    /**
     * Extract a file from the jar (located in src/main/resources)
     * @param loader A class loader. When using this function from another jar, it needs that class loader. Can be
     * any class inside the jar you want to extract from.
     * @param resourcesPath The source path from within the jar (src/main/resources/[resourcesPath]).
     * @param destination The destination of the extracted file.
     * @param overwrite To overwrite or not to overwrite.
     * @return Whether the file was successfully extracted
     */
    fun extractFile(loader: ClassLoader, resourcesPath: String, destination: Path, overwrite: Boolean = false): Boolean {
        val exists: Boolean = destination.exists()
        return try {
            if (overwrite || !exists) {
                if (!exists) destination.parent.toFile().mkdirs()
                val stream = loader.getResourceAsStream(resourcesPath) ?: return false
                Files.copy(stream, destination, StandardCopyOption.REPLACE_EXISTING)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun fixFileName(fileName: String): String {
        val x = fileName.replace(cleanFileNameRegex, "")
        return x.take(x.length.coerceAtMost(128))
    }
}