package co.akoot.bluefox.api.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

object TimeUtil {
    val today: Long get() = LocalDate.now().toEpochDay()

    val month get() = Calendar.getInstance().get(Calendar.MONTH)

    val daysInMonth get() = YearMonth.now().lengthOfMonth()
    val daysInYear get() = YearMonth.now().lengthOfYear()
    private val timeRegex = Regex("((?:[0-9]*[.])?[0-9]+)([a-z]{1,2})")

    private val timeMapMillis = mapOf(
        "ms" to 1,
        "t" to 50,
        "s" to 1000,
        "m" to 60000,
        "h" to 3600000,
        "d" to 86400000,
        "w" to 604800000,
        "mo" to daysInMonth * 86400000,
        "y" to daysInYear * 86400000
    )

    private val timeMapMillisLong = mapOf(
        "milliseconds" to 1,
        "ticks" to 50,
        "seconds" to 1000,
        "minutes" to 60000,
        "hours" to 3600000,
        "days" to 86400000,
        "weeks" to 604800000,
        "months" to daysInMonth * 86400000,
        "years" to daysInYear * 86400000
    )

    private val timeMapTicks = mapOf(
        "t" to 1,
        "s" to 20,
        "m" to 1200,
        "h" to 72000,
        "d" to 1728000,
        "w" to 12096000,
        "mo" to daysInMonth * 1728000,
        "y" to daysInYear * 1728000
    )

    const val FORMAT_SIMPLE = "MM-dd"
    const val FORMAT_SIMPLE_YEAR = "MM-dd yyyy"
    const val FORMAT_SIMPLE_TIME = "MM-dd hh:mma"

    fun parseTime(string: String, asTicks: Boolean = false): Long {
        var totalTime = 0L

        for (result in timeRegex.findAll(string)) {
            if (result.groupValues.size != 3) continue
            val number = result.groupValues[1].toLongOrNull() ?: continue
            val multiplier =
                (if (asTicks) timeMapTicks[result.groupValues[2]] else timeMapMillis[result.groupValues[2]])
                    ?: continue
            totalTime += number * multiplier
        }

        return totalTime
    }

    fun formatTime(
        milliseconds: Long,
        pattern: String = FORMAT_SIMPLE_TIME,
        timeZone: TimeZone = TimeZone.getDefault()
    ): String {
        val dateFormat = SimpleDateFormat(pattern)
        dateFormat.timeZone = timeZone
        return dateFormat.format(Date(milliseconds))
    }

    fun getTimeString(milliseconds: Long): String {
        if (milliseconds == 0L) return "0 seconds" // Handle special case of zero

        val timeUnits = listOf("days", "hours", "minutes", "seconds")
        val timeValues = listOf(
            86400000L,
            3600000L,
            60000L,
            1000L
        ) // Milliseconds in a day, hour, minute, second, and millisecond

        var remainingTime = milliseconds
        val result = StringBuilder()

        for ((index, unit) in timeUnits.withIndex()) {
            val value = remainingTime / timeValues[index]
            if (value > 0) {
                result.append("$value ${if (value == 1L) unit.substringBefore("s") else unit} ")
                remainingTime %= timeValues[index]
            }
        }

        return result.trim().toString()
    }

    fun parseDateTime(
        string: String,
        timeZone: TimeZone = TimeZone.getDefault(),
        now: Long = System.currentTimeMillis()
    ): Long? {
        return when (string) {
            "tomorrow" -> now + parseTime("1d")
            "nextWeek" -> now + parseTime("1w")
            "nextMonth" -> now + parseTime("1mo")
            "nextYear" -> now + parseTime("1y")
            else -> {
                val (pattern, format) = when (string.count { it == '-' }) {
                    4 -> Pair(string.substring(0, string.lastIndexOf("-")), "yyyy-MM-dd-hh:mma")
                    3 -> Pair(string, "yyyy-MM-dd-hh:mma")
                    2 -> Pair(string, "yyyy-MM-dd")
                    0 -> Pair(string, "hh:mma")
                    else -> return null
                }
                val formatter = DateTimeFormatter.ofPattern(format)
                val localDateTime = LocalDateTime.parse(pattern, formatter)
                val zoneId = timeZone.toZoneId()
                localDateTime.atZone(zoneId).toInstant().toEpochMilli()
            }
        }
    }

    fun parseTime(string: String): Long {
        var totalTime = 0L
        for (result in timeRegex.findAll(string)) {
            if (result.groupValues.size != 3) continue
            val number = result.groupValues[1].toLongOrNull() ?: continue
            val multiplier = timeMapMillis[result.groupValues[2]] ?: continue
            totalTime += number * multiplier
        }
        return totalTime
    }
}