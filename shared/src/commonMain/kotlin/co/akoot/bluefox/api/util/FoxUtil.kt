package co.akoot.bluefox.api.util

object FoxUtils {
    var debug: Boolean = false
}

/**
 * Send a debug message.
 *
 * Only sends if FoxUtils.debug = false
 * @param msg Anything
 */
fun debug(msg: Any?) {
    if(!FoxUtils.debug) return
    print("[DEBUG] ")
    println(msg)
}

/**
 * Send a debug message.
 *
 * Only sends if FoxUtils.debug = false
 * @param msg Anything
 * @param prefix A prefix to add to the debug message
 */
fun debug(prefix: String, msg: String) {
    if(!FoxUtils.debug) return
    print("[DEBUG] ")
    print("$prefix: ")
    println(msg)
}

/**
 * Get something by running [block] while catching.
 * @return The result of [block] if there weren't any exceptions, otherwise null
 */
fun <T> get(block: () -> T?): T? {
    return runCatching(block).getOrNull()
}

/**
 * Sends a debug message containing this object
 * @param transform The transformation applied to this object before printing
 * @return This object without modification
 */
fun <T> T.debug(transform: (T) -> Any? = { this }): T {
    val string = transform(this)
    debug(string)
    return this
}

/**
 * Sends a debug message containing this object
 * @param prefix A prefix to add to the debug message
 * @param transform The transformation applied to this object before printing
 * @return This object without modification
 */
fun <T> T.debug(prefix: String, transform: (T) -> Any? = { this }): T {
    val string = transform(this).toString()
    debug(prefix, string)
    return this
}