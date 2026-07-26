package co.akoot.bluefox.api.delegate

import co.akoot.bluefox.api.config.FoxConfig
import co.akoot.bluefox.api.delegate.backing.ConfigBacking
import co.akoot.bluefox.api.delegate.backing.DelegateBacking
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.typeOf

class Delegate<T>(
    private val backend: DelegateBacking,
    private var default: T? = null,
    private var parent: String? = null,
    private val fromString: ((String) -> T)? = null,
    private val toString: ((T) -> String)? = { it.toString() },
) : ReadWriteProperty<Any?, T> {

    constructor(config: FoxConfig, default: T? = null) : this(ConfigBacking(config), default)

    companion object {
        val namespaceRegistry = mutableMapOf<KProperty<*>, String>()
        val keyRegistry = mutableMapOf<KProperty<*>, String>()
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val namespace = namespaceRegistry.getOrPut(property) { property.findNamespace() }
        val key = keyRegistry.getOrPut(property) { property.findKey() }

        val value: T? = if (fromString != null) {
            backend.get<String>(namespace, key, typeOf<String>())?.let(fromString)
        } else {
            val type = property.returnType

            if (type.classifier == List::class) {
                @Suppress("UNCHECKED_CAST")
                backend.getList(
                    namespace,
                    key,
                    type.arguments.firstOrNull()?.type
                ) as T?
            } else {
                backend.get(namespace, key, type)
            }
        } ?: default

        return value
            ?: error("Missing config value for '${backend.getRoot()}$key' and no default provided")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val namespace = namespaceRegistry.getOrPut(property) { property.findNamespace() }
        val key = keyRegistry.getOrPut(property) { property.findKey() }

        if (toString != null) {
            backend.set(namespace, key, toString(value))
            return
        }

        val type = property.returnType

        if (type.classifier == List::class) {
            @Suppress("UNCHECKED_CAST")
            backend.setList(
                namespace,
                key,
                value as List<*>,
                type.arguments.firstOrNull()?.type
            )
        } else {
            backend.set(namespace, key, value)
        }
    }

    private fun KProperty<*>.findNamespace(): String =
        annotations.filterIsInstance<Key>().firstOrNull()?.namespace
            ?: ""

    private fun KProperty<*>.findKey(): String =
        annotations.filterIsInstance<Key>().firstOrNull()?.path
            ?: "${parent?.let { "$it." } ?: ""}$name"

    infix fun <R> of(fromString: (String) -> R): Delegate<R> =
        Delegate(
            backend = backend,
            parent = parent,
            fromString = fromString
        )

    infix fun <R> from(toString: (R) -> String): Delegate<R> =
        Delegate(
            backend = backend,
            toString = toString
        )

    infix fun default(default: T): Delegate<T> = this.apply { this.default = default }

    infix fun from(parent: String): Delegate<T> = this.apply { this.parent = parent }
}