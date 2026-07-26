package co.akoot.bluefox.api.delegate.backing

import co.akoot.bluefox.api.config.FoxConfig
import co.akoot.bluefox.api.delegate.Delegate
import kotlin.reflect.KType

class ConfigBacking(private val backing: FoxConfig) :
    DelegateBacking {
    override fun <T> get(namespace: String, key: String, type: KType?): T? {
        @Suppress("UNCHECKED_CAST")
        return when (type?.classifier) {
            String::class -> backing.getString(key)
            Int::class -> backing.getInt(key)
            Boolean::class -> backing.getBoolean(key)
            Double::class -> backing.getDouble(key)
            Long::class -> backing.getLong(key)
            else -> null
        } as T?
    }

    override fun <T> set(namespace: String, key: String, value: T?) {
        backing.set(key, value)
    }

    override fun getList(namespace: String, key: String, type: KType?): List<*>? {
        @Suppress("UNCHECKED_CAST")
        return when (type?.classifier) {
            String::class -> backing.getStringList(key)
            Int::class -> backing.getIntList(key)
            Boolean::class -> backing.getBooleanList(key)
            Double::class -> backing.getDoubleList(key)
            Long::class -> backing.getLongList(key)
            else -> null
        } as List<*>?
    }

    override fun setList(
        namespace: String,
        key: String,
        list: List<*>,
        elementType: KType?
    ) {
        backing.set(key, list)
    }

    override fun getRoot(): String = "${backing.file.path}:"
}

infix fun <T> FoxConfig.default(default: T? = null): Delegate<T> =
    Delegate(this, default)
infix fun <T> FoxConfig.of(transform: (String) -> T): Delegate<T> =
    Delegate(ConfigBacking(this), fromString = transform)

infix fun <T> FoxConfig.from(parent: String?): Delegate<T> =
    Delegate(
        ConfigBacking(this),
        parent = parent?.takeIf { it.isNotEmpty() })
