package co.akoot.bluefox.api.config

import co.akoot.bluefox.api.util.StringUtil
import com.google.gson.JsonObject
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigRenderOptions
import com.typesafe.config.ConfigValueFactory
import java.util.UUID

open class FoxConf(content: String) {

    protected var config: Config = ConfigFactory.parseString(content)
    protected val options: ConfigRenderOptions = ConfigRenderOptions.concise().setFormatted(true)

    fun set(path: String, value: Any?) {
        config = config.withValue(path, ConfigValueFactory.fromAnyRef(value))
        set()
    }

    fun getKeys(path: String? = null): MutableSet<String> {
        get()
        return if (path == null) config.root().keys
        else config.getConfig(path)?.root()?.keys ?: mutableSetOf()
    }

    // Generic function for single values
    private inline fun <reified T> get(path: String, getter: (Config, String) -> T): T? {
        get()
        return runCatching { getter(config, path) }.getOrNull()
    }

    // Generic function for list values
    private inline fun <reified T> getList(path: String, getter: (Config, String) -> List<T>): List<T> {
        get()
        return runCatching { getter(config, path) }.getOrDefault(emptyList())
    }

    // Getters for Enums
    fun <E: Enum<E>> getEnum(enumClass: Class<E>, path: String): E? {
        get()
        return runCatching { config.getEnum(enumClass, path)}.getOrNull()
    }
    fun <E: Enum<E>> getEnumList(enumClass: Class<E>, path: String): List<E> {
        get()
        return runCatching { config.getEnumList(enumClass, path)}.getOrNull() ?: mutableListOf()
    }

    // Getters for everything else
    fun getString(path: String) = get(path, Config::getString)
    fun getStringList(path: String) = getList(path, Config::getStringList)

    fun getLong(path: String) = get(path, Config::getLong)
    fun getLongList(path: String) = getList(path, Config::getLongList)

    fun getInt(path: String) = get(path, Config::getInt)
    fun getIntList(path: String) = getList(path, Config::getIntList)

    fun getDouble(path: String) = get(path, Config::getDouble)
    fun getDoubleList(path: String) = getList(path, Config::getDoubleList)

    fun getBoolean(path: String) = get(path, Config::getBoolean)
    fun getBooleanList(path: String) = getList(path, Config::getBooleanList)

    fun <T> getFromString(path: String, transform: (String) -> T) = get(path, Config::getString)?.let { transform(it) }
    fun <T> getFromStringList(path: String, transform: (String) -> T) = getList(path, Config::getStringList).map { transform(it) }

    inline fun <reified T : Any> append(path: String, item: T) {
        val list = when(T::class) {
            UUID::class -> getFromString(path) { UUID.fromString(it) }
            Long::class -> getLongList(path)
            Int::class -> getIntList(path)
            Double::class -> getDoubleList(path)
            Float::class -> getDoubleList(path).map { it.toFloat() }
            Boolean::class -> getBooleanList(path)
            else -> getStringList(path)
        } as List<*>
        set(path, list + item)
    }

    inline fun <reified T : Any> remove(path: String, item: T) {
        val list = when(T::class) {
            UUID::class -> getFromStringList(path) { UUID.fromString(it) }
            Long::class -> getLongList(path)
            Int::class -> getIntList(path)
            Double::class -> getDoubleList(path)
            Float::class -> getDoubleList(path).map { it.toFloat() }
            Boolean::class -> getBooleanList(path)
            else -> getStringList(path)
        } as List<T>
        set(path, list - item)
    }

    fun increment(path: String, amount: Long = 1, max: Long = Long.MAX_VALUE) {
        val value = getLong(path) ?: 0
        set(path, (value + amount).coerceAtMost(max))
    }

    fun increment(path: String, amount: Int = 1, max: Int = Int.MAX_VALUE) {
        val value = getInt(path) ?: 0
        set(path, (value + amount).coerceAtMost(max))
    }

    fun increment(path: String, amount: Double = .01, max: Double = Double.MAX_VALUE) {
        val value = getDouble(path) ?: 0.0
        set(path, (value + amount).coerceAtMost(max))
    }

    fun decrement(path: String, amount: Long = 1, min: Long = 0) {
        val value = getLong(path) ?: 0
        set(path, (value - amount).coerceAtLeast(min))
    }

    fun decrement(path: String, amount: Int = 1, min: Int = 0) {
        val value = getInt(path) ?: 0
        set(path, (value - amount).coerceAtLeast(min))
    }

    fun decrement(path: String, amount: Double = .01, min: Double = 0.0) {
        val value = getDouble(path) ?: 0.0
        set(path, (value - amount).coerceAtLeast(min))
    }

    fun toJson(): JsonObject {
        return StringUtil.jsonObject(config.root().render()) ?: JsonObject()
    }

    protected open fun set() {}
    protected open fun get() {}
}