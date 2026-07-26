package co.akoot.bluefox.api.util

import co.akoot.bluefox.api.extensions.f
import co.akoot.bluefox.api.extensions.spaceAfter

object Timer {
    private val timers: MutableMap<String, Long> = mutableMapOf()

    fun start(timer: String = ""): Long {
        val now = System.currentTimeMillis()
        timers[timer] = now
        return now
    }

    fun elapsed(timer: String = "", reset: Boolean = true): Long {
        val now = System.currentTimeMillis()
        val start = timers.getOrElse(timer) { 0L }
        if(reset) timers.remove(timer)
        return now - start
    }

    fun elapsedString(timer: String = "", reset: Boolean = true): String {
        val elapsed = elapsed(timer, reset)
        return "${elapsed}ms (${(elapsed / 1000.0).f}s)"
    }

    fun printElapsed(timer: String = "", reset: Boolean = true, prefix: String = "${timer.spaceAfter}took ") {
        println("$prefix${elapsedString(timer, reset)}")
    }

    fun debugElapsed(timer: String = "", reset: Boolean = true, prefix: String = "${timer.spaceAfter}took ") {
        debug("$prefix${elapsedString(timer, reset)}")
    }
}