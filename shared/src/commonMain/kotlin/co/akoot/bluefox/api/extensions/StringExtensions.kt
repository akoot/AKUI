package co.akoot.bluefox.api.extensions

import kotlin.text.iterator

val String.wordCount: Int get() = this.split(" ").size

val String.escapeJson: String
    get() {
        val sb = StringBuilder(length + 16)

        for (c in this) {
            when (c) {
                '"' -> sb.append("\\\"")
                '\\' -> sb.append("\\\\")
                '\b' -> sb.append("\\b")
                '\u000C' -> sb.append("\\f") // form feed
                '\n' -> sb.append("\\n")
                '\r' -> sb.append("\\r")
                '\t' -> sb.append("\\t")
                else -> {
                    if (c < ' ') {
                        sb.append("\\u%04x".format(c.code))
                    } else {
                        sb.append(c)
                    }
                }
            }
        }

        return sb.toString()
    }

val String.unescapeJson: String
    get() {
        val sb = StringBuilder(length)

        var i = 0
        while (i < length) {
            val c = this[i]

            if (c == '\\' && i + 1 < length) {
                when (val next = this[i + 1]) {
                    '"' -> sb.append('"')
                    '\\' -> sb.append('\\')
                    '/' -> sb.append('/')
                    'b' -> sb.append('\b')
                    'f' -> sb.append('\u000C')
                    'n' -> sb.append('\n')
                    'r' -> sb.append('\r')
                    't' -> sb.append('\t')
                    'u' -> {
                        if (i + 5 < length) {
                            val hex = substring(i + 2, i + 6)
                            sb.append(hex.toInt(16).toChar())
                            i += 4
                        } else {
                            sb.append("\\u")
                        }
                    }

                    else -> {
                        // invalid escape, keep as-is
                        sb.append('\\').append(next)
                    }
                }
                i += 2
            } else {
                sb.append(c)
                i++
            }
        }

        return sb.toString()
    }

val String.escapeNewLines: String get() = this.replace("\n", "\\n")
val String.unescapeNewLines: String get() = this.replace("\\n", "\n")

fun String.append(string: String?, separator: String = "\n\n"): String =
    string?.let { "$this$separator$string" } ?: this

val String.spaceAfter: String get() = if(isBlank()) this else "$this "
val String.spaceBefore: String get() = if(isBlank()) this else " $this"
val String.spaceBetween: String get() = if(isBlank()) this else " $this "
