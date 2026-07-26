package co.akoot.bluefox.api

import com.google.gson.JsonObject
import java.util.UUID

object BlueFoxApi {
    const val SECRET = ":o"
}

fun JsonObject.put(key: String, value: Any): JsonObject {
    when(value) {
        is String -> this.addProperty(key, value)
        is Int -> this.addProperty(key, value)
        is Char -> this.addProperty(key, value)
        is Long -> this.addProperty(key, value)
        is Double -> this.addProperty(key, value)
        is Boolean -> this.addProperty(key, value)
        is UUID -> this.addProperty(key, value.toString())
        else -> throw IllegalArgumentException("Unsupported type for JSON property: ${value::class.simpleName}")
    }
    return this
}