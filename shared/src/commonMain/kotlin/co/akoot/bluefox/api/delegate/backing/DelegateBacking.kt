package co.akoot.bluefox.api.delegate.backing

import kotlin.reflect.KType

interface DelegateBacking {
    fun <T> get(namespace: String, key: String, type: KType?): T?
    fun <T> set(namespace: String, key: String, value: T?)
    fun getList(namespace: String, key: String, type: KType?): List<*>?
    fun setList(namespace: String, key: String, list: List<*>, elementType: KType?)

    fun getRoot(): String
}