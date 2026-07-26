package co.akoot.bluefox.api.config

import co.akoot.bluefox.api.extensions.touch
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigRenderOptions
import com.typesafe.config.ConfigValueFactory
import java.io.File
import java.util.UUID

open class FoxConfig(val file: File): FoxConf(file.readText()) {

    var autoload = true
    var autosave = true

    /**
     * Loads the config file into memory
     */
    private fun load() {
        config = ConfigFactory.parseFile(file)
    }

    /**
     * Unloads the config file by setting it to an empty config
     */
    fun unload() {
        config = ConfigFactory.empty()
    }

    /**
     * Loads the config file into memory
     * (Semantics WIN!)
     */
    fun reload() {
        load()
    }

    /**
     * Saves the config in memory to the config file
     */
    fun save() {
        file.touch()
        file.writeText(config.root().render(options))
    }

    fun save(block: FoxConfig.() -> Unit) {
        val wasAutosave = autosave
        autosave = false
        block()
        autosave = wasAutosave
        save()
    }

    override fun set() {
        if(autosave) save()
    }

    override fun get() {
        if(autoload) load()
    }
}