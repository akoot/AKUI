package co.akoot.bluefox.api.util

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.TreeMap

object StringUtil {
    val romanNumerals = TreeMap<Int, String>().apply {
        put(1000, "M")
        put(900, "CM")
        put(500, "D")
        put(400, "CD")
        put(100, "C")
        put(90, "XC")
        put(50, "L")
        put(40, "XL")
        put(10, "X")
        put(9, "IX")
        put(5, "V")
        put(4, "IV")
        put(1, "I")
    }

    fun toRomanNumeral(number: Int): String? {
        val l: Int = romanNumerals.floorKey(number)
        if (number == l) {
            return romanNumerals[number]
        }
        val romanNumeral = romanNumerals[l] ?: return null
        val result = toRomanNumeral(number - l) ?: return null
        return romanNumeral + result
    }

    private val replacementRegex = Regex("""\$(\w+)?\{([^}]+)\}""")

    fun replace(input: String, replacements: Map<String, String>? = null, default: String = "(UNKNOWN)"): String =
        replacementRegex.replace(input) { match ->
            val type = match.groupValues[1]
            val pattern = match.groupValues[2]

            when (type) {
                "time" -> LocalTime.now().format(DateTimeFormatter.ofPattern(pattern))
                "date" -> LocalDate.now().format(DateTimeFormatter.ofPattern(pattern))
                "rand" -> pattern.split(",", ", ").random()
                else -> {
                    if (replacements == null) default
                    else replacements[pattern] ?: default
                }
            }
        }

    fun getDelay(wpm: Int, text: String): Long {
        val words = text.split(" ", "—", "\n").filterNot { it.isEmpty() }
        var delay: Long = 0
        for (word in words) {
            delay += word.length
        }
        val result = delay * 10000 / wpm
        return result.coerceAtLeast(3000)
    }

    private val gson = Gson()

    fun <T> json(string: String, classOfT: Class<T>): T? {
        return get { gson.fromJson(string, classOfT) }
    }

    fun jsonObject(string: String?): JsonObject? {
        string ?: return null
        return get { json(string, JsonObject::class.java) }
    }

    fun jsonArray(string: String?): JsonArray? {
        string ?: return null
        return get { json(string, JsonArray::class.java) }
    }
}

class CommandString(string: String) {

    class Parameter(val flag: String? = null, argument: String? = null) {
        val hasFlag = flag != null
        val hasContent = argument != null
        val content = argument?.removeSurrounding("\"")?.removeSurrounding("'")?.removeSurrounding("`")
        override fun toString(): String {
            return if(hasFlag) {
                if(hasContent) "$flag=$content"
                else "$flag=true"
            } else content ?: ""
        }
    }

    private val argumentRegex = Regex("""(?:--?(\w+)[:=]? ?)?("(?:[^"\\]|\\.)*"|'(?:[^'\\]|\\.)*'|`(?:[^`\\]|\\.)*`|\w+)?""")
    val command = string.substringBefore(" ")
    val argumentString = string.substringAfter(" ")
    val parameters = argumentRegex.findAll(argumentString).mapNotNull {
        val flag = it.groups[1]?.value
        val content = it.groups[2]?.value
        if(flag == null && content == null) null else Parameter(flag, content)
    }

    val flags get() = parameters.filter { it.flag != null && it.content == null }
    val enabledFlags get() = flags.filter { it.content == null }

    val args get() = parameters.map { it.toString() }.toList()

    override fun toString() = "/$command " + args.joinToString(" ") { "[$it]" }

    fun hasFlag(flag: String) = flag in enabledFlags.map { it.flag }
    fun getFlag(flag: String) = flags.find { it.flag == flag }
    operator fun get(index: Int) = parameters.toList().getOrNull(index)
}